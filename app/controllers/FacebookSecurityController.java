package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.FacebookData;
import models.user.User;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.*;
import play.mvc.Http.*;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Endpoint controller for handling requests for Facebook authorization.
 *
 * @author Simon Olofsson
 */
public class FacebookSecurityController extends Controller {

    public static final String FACEBOOK_AUTH_TOKEN_BODY_FIELD = "facebookAuthToken";
    public static final String AUTH_TOKEN = "authToken";

    private WSClient ws;

    private HttpExecutionContext ec;

    @Inject
    public FacebookSecurityController(WSClient ws, HttpExecutionContext ec) {

        this.ws = ws;
        this.ec = ec;

    }

    public static User getUser() {
        return (User) Http.Context.current().args.get("tenant");
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

            Result result = ResponseBuilder.buildBadRequest("Request body required", ResponseBuilder.MALFORMED_REQUEST_BODY);
            return CompletableFuture.completedFuture(result);

        }

        return ws.url("https://graph.facebook.com/me?access_token=" + facebookToken).get()
                .thenApplyAsync(userData -> { // thenApplyAsync is needed if an HttpExecutionContext needs to be passed, see comment below.

                    if (userData.asJson().findValue("error") != null) {
                        return status(userData.getStatus(), userData.asJson());
                    }

                    String userToken = processUserData(facebookToken, userData);

                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode responseJson = mapper.createObjectNode();

                    responseJson.put(AUTH_TOKEN, userToken);
                    setAuthTokenCookie(userToken);

                    return ok(responseJson);

                }, ec.current()); // Passing HttpExecutionContext to be able to set cookies, context not otherwise available in async calls.

    }

    private void setAuthTokenCookie(String authToken) {

        ObjectNode authTokenJson = Json.newObject();
        authTokenJson.put(AUTH_TOKEN, authToken);
        response().setCookie(Cookie.builder(AUTH_TOKEN, authToken).withSecure(request().secure()).build());

    }

    /**
     * Takes data from Facebook and determines whether the tenant already exists
     * or not, and creates or updates the tenant and its fields accordingly.
     *
     * @param facebookToken a Facebook authorization token.
     * @param userData a WSResponse object containing the tenant data.
     * @return the tenant id extracted from the data.
     */
    private String processUserData(String facebookToken, WSResponse userData) {

        JsonNode jsonData = userData.asJson();
        String email = jsonData.findValue("email").textValue();

        User user = User.findByEmailAddress(email);

        if (user == null) {

            user = new User(email, jsonData.findValue("name").textValue());
            user.save();

        }

        setUserAttributes(user, userData.asJson(), facebookToken);
        return user.createToken();

    }

    /**
     * Sets tenant attributes according to the data passed to the method. Only
     * operates on existing users and will create a new FacebookData entry
     * from the data passed.
     *
     * @param user the tenant to operate on.
     * @param userData the data to create a FacebookData entry from.
     * @param facebookToken the Facebook authorization token used to
     *                      obtain the data. Will be included in the
     *                      FacebookData entry for further use.
     */
    private void setUserAttributes(User user, JsonNode userData, String facebookToken) {

        FacebookData fbData = buildFaceBookData(userData);
        fbData.save();

        user.setFacebookData(fbData);
        user.setFacebookAuthToken(facebookToken);

        user.save();

    }

    private FacebookData buildFaceBookData(JsonNode data) {

        String id = data.findValue("id").textValue();

        FacebookData fbData = FacebookData.findByFacebookUserId(id);
        if (fbData == null) {
            fbData = new FacebookData();
        }

        fbData.facebookUserId = id;
        fbData.emailAddress = data.findValue("email").textValue();
        fbData.firstName = data.findValue("first_name").textValue();
        fbData.lastName = data.findValue("last_name").textValue();
        fbData.gender = data.findValue("gender").textValue();
        fbData.locale = data.findValue("locale").textValue();
        fbData.timezone = data.findValue("timezone").intValue();

        return fbData;

    }

    @Security.Authenticated(Secured.class)
    public Result logout() {

        response().discardCookie(AUTH_TOKEN);
        getUser().deleteAuthToken();

        return redirect("/");

    }
}

