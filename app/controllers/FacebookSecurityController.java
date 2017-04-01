package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import models.FacebookData;
import models.User;
import play.libs.concurrent.HttpExecutionContext;
import play.data.validation.Constraints;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.*;
import play.mvc.Http.*;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Endpoint controller for handling requests for Facebook authorization.
 *
 * @author Simon Olofsson
 */
public class FacebookSecurityController extends Controller {

    public static final String AUTH_TOKEN_HEADER = "FACEBOOK-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";

    private String userToken;
    private String userId;

    @Inject
    WSClient ws;

    @Inject
    HttpExecutionContext ec;

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

    /**
     * Method for handling a request for authorization via Facebook. Accepts a
     * Facebook access token, collects basic user data and uses the user id to
     * make further requests for data to Facebook. Currently, the only further
     * data collected is the first page of the user's list of friends. Sends
     * a custom authentication token back on successful requests, to be used
     * for further communication.
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

        String facebookToken = request().getHeader(AUTH_TOKEN_HEADER);

        return ws.url("https://graph.facebook.com/me?access_token=" + facebookToken).get()
                .thenCompose(userData -> {

                    if (userData.asJson().findValue("error") != null) {
                        return CompletableFuture.completedFuture(userData);
                    }

                    String userId = processUserData(facebookToken, userData);
                    return ws.url("https://graph.facebook.com/" + userId + "/friendlists?access_token=" + facebookToken).get();

                }).thenApplyAsync(response -> { // thenApplyAsync is needed if an HttpExecutionContext needs to be passed, see comment below.

                    if (response.getStatus() != OK) {
                        return status(response.getStatus(), response.asJson());
                    }

                    setAuthTokenCookie();
                    return ok(response.asJson());

                }, ec.current()); // Passing HttpExecutionContext to be able to set cookies, context not otherwise available in async calls.
    }

    private void setAuthTokenCookie() {

        ObjectNode authTokenJson = Json.newObject();
        authTokenJson.put(AUTH_TOKEN, userToken);
        response().setCookie(Cookie.builder(AUTH_TOKEN, userToken).withSecure(request().secure()).build());

    }

    /**
     * Takes data from Facebook and determines whether the user already exists
     * or not, and creates or updates the user and its fields accordingly.
     *
     * @param facebookToken a Facebook authorization token.
     * @param userData a WSResponse object containing the user data.
     * @return the user id extracted from the data.
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
        String userId = userData.asJson().findValue("id").textValue();
        userToken = user.createToken();

        return userId;

    }

    /**
     * Sets user attributes according to the data passed to the method. Only
     * operates on existing users and will create a new FacebookData entry
     * from the data passed.
     *
     * @param user the user to operate on.
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
        String emailAddress = data.findValue("email").textValue();
        String firstName = data.findValue("first_name").textValue();
        String lastName = data.findValue("last_name").textValue();
        String gender = data.findValue("gender").textValue();
        String locale = data.findValue("locale").textValue();
        int timeZone = data.findValue("timezone").intValue();

        return new FacebookData(id, emailAddress, firstName, lastName, gender, locale, timeZone);

    }

    @Security.Authenticated(Secured.class)
    public Result logout() {

        response().discardCookie(AUTH_TOKEN);
        getUser().deleteAuthToken();

        return redirect("/");

    }
}

