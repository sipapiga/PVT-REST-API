package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.*;
import scala.Function1;
import scala.concurrent.Future;
import scala.concurrent.Promise;

import javax.inject.Inject;
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

            WSRequest request = ws.url("https://graph.facebook.com/me?access_token=" + facebookToken);
            return request.get().thenApply(response ->
                // check if user exists or not and record accordingly
                ok(response.asJson())
            );
        }
        return null;
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

