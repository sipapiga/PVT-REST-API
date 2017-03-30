package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import jdk.nashorn.internal.ir.RuntimeNode;
import models.User;
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
import scala.Function1;
import scala.concurrent.Future;
import scala.concurrent.Promise;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class FacebookSecurityController extends Controller {

    public static final String AUTH_TOKEN_HEADER = "FACEBOOK-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";

    @Inject
    WSClient ws;

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

    public CompletionStage<Result> login() {

        String facebookToken = request().getHeader(AUTH_TOKEN_HEADER);

        if ((facebookToken != null) && !facebookToken.isEmpty()) {

            /*
             * 1. Check if user already exists, update or create accordingly.
             * 2. Get new session token.
             * 3. Make request to Facebook graph api and get all desired fields.
             * 4. Update user and save.
             * 5. Return session token and data to the client.
             */

            WSRequest request = ws.url("https://graph.facebook.com/me?access_token=" + facebookToken);
            return request.get().thenCompose(userData -> {

                JsonNode jsonData = userData.asJson();
                String email = jsonData.findValue("email").textValue();
                String name = jsonData.findValue("name").textValue();

                User user = User.findByEmailAddress(email);

                if (user == null) {
                    user = new User(email, "password", name);
                }

                String token = user.createToken();
                String userId = userData.asJson().findValue("id").textValue();

               return getRelevantFields(userId, facebookToken).thenApply(response -> ok(response.asJson()));

            });
        } else {

            // ToDo: This should return unauthorized. Figure out how to return it as a CompletionStage.
            return null;
        }
    }

    private CompletionStage<WSResponse> getRelevantFields(String userId, String accessToken) {

        String url = "https://graph.facebook.com/" + userId + "/friendlists?access_token=" + accessToken;
        return ws.url("https://graph.facebook.com/" + userId + "/friendlists?access_token=" + accessToken)
        .get();
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

