package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.FacebookData;
import models.user.User;
import play.Configuration;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.*;
import play.mvc.Http.*;
import services.facebook.FacebookService;
import services.users.UsersService;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Endpoint controller for handling requests for Facebook authorization.
 *
 *
 * @author Simon Olofsson
 */
public class FacebookSecurityController extends Controller {

    public static final String FACEBOOK_AUTH_TOKEN_BODY_FIELD = "facebookAuthToken";
    public static final String AUTH_TOKEN = "authToken";

    private String appId;
    private String appToken;
    private String appName;

    private static final String FIELDS = "email,first_name,last_name,gender,locale,name,timezone";

    private WSClient ws;
    private HttpExecutionContext ec;
    private Configuration config;

    private FacebookService fbService;
    private UsersService usersService;

    @Inject
    public FacebookSecurityController(WSClient ws, HttpExecutionContext ec, Configuration config,
                                      FacebookService fbService,
                                      UsersService usersService) {

        this.ws = ws;
        this.ec = ec;
        this.config = config;

        this.fbService = fbService;
        this.usersService = usersService;

        appId = config.getString("appId");
        appToken = config.getString("appToken");
        appName = config.getString("appName");

    }

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

    /**
     * Method for handling a request for authorization via Facebook. Accepts a
     * Facebook access token, collects basic tenant data and uses the tenant id to
     * make further requests for data to Facebook. Currently, the only further
     * data collected is the first page of the tenant's list of friends. Sends
     * a custom authentication token back on successful requests, to be used
     * for authenticating further requests to the server.
     *
     * The access token should be provided in the header field represented by
     * the AUTH_TOKEN_HEADER constant and the custom authentication token
     * returned will be placed in the field represented by the AUTH_TOKEN
     * constant.
     *
     * @return a status code and, given that the authorization was successful,
     * a custom authentication token to be used in future communication with
     * the server.
     */
    public CompletionStage<Result> login() {

        String facebookToken;

        try {

            JsonNode body = request().body().asJson();
            facebookToken = body.findValue(FACEBOOK_AUTH_TOKEN_BODY_FIELD).asText();

        } catch (NullPointerException e) {

            Result result = ResponseBuilder.buildBadRequest("Request body required.", ResponseBuilder.MALFORMED_REQUEST_BODY);
            return CompletableFuture.completedFuture(result);

        }

        return fbService.inspectionRequest(facebookToken, appId, appToken, appName).thenApplyAsync(facebookData -> {

            if (facebookData == null) {
                return ResponseBuilder.buildInternalServerError("Facebook data could not be processed.");
            }

            JsonNode facebookJson = facebookData.asJson();

            if (facebookJson.findValue("error") != null) {
                return badRequest(facebookData.asJson());
            }

            User user = usersService.createFromFacebookData(facebookJson);
            String userToken = usersService.getToken(user);

            ObjectNode responseJson = Json.newObject();

            responseJson.put(AUTH_TOKEN, userToken);
            setAuthTokenCookie(userToken);

            return ok(responseJson);

        }, ec.current());
    }

    private void setAuthTokenCookie(String authToken) {

        ObjectNode authTokenJson = Json.newObject();
        authTokenJson.put(AUTH_TOKEN, authToken);
        response().setCookie(Cookie.builder(AUTH_TOKEN, authToken).withSecure(request().secure()).build());

    }

    @Security.Authenticated(Secured.class)
    public Result logout() {

        response().discardCookie(AUTH_TOKEN);
        getUser().deleteAuthToken();

        return redirect("/");

    }
}

