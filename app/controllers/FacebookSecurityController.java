package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import models.FacebookData;
import models.User;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.oauth.OAuth.RequestToken;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.libs.oauth.OAuth;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.libs.ws.WSSignatureCalculator;
import play.mvc.*;
import play.mvc.Http.*;
import scala.Function1;
import scala.concurrent.Future;
import scala.concurrent.Promise;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

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

    public CompletionStage<Result> login() {

        String facebookToken = request().getHeader(AUTH_TOKEN_HEADER);

        CompletionStage<WSResponse> atRes = ws.url("https://graph.facebook.com/me?access_token=" + facebookToken).get();
        CompletionStage<WSResponse> mdRes = atRes.thenCompose(userData -> {

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

            return ws.url("https://graph.facebook.com/" + userId + "/friendlists?access_token=" + facebookToken).get();

        });

        return mdRes.thenApplyAsync(res -> {

            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, userToken);
            response().setCookie(Cookie.builder(AUTH_TOKEN, userToken).withSecure(request().secure()).build());

            return ok(res.asJson());

        }, ec.current());

        // how about this for handling unauthorized requests:
        // return CompletableFuture.completedFuture(unathorized())?
        // see comment at http://stackoverflow.com/questions/38287403/returning-common-result-as-completionstageresult-in-play

    }

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

    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String emailAddress;

        @Constraints.Required
        public String password;

    }
}

