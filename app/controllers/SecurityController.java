/*
 * From https://github.com/jamesward/play-rest-security
 */

package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.*;
import play.Logger;

import javax.inject.Inject;

public class SecurityController extends Controller {

    @Inject
    FormFactory formFactory;

    public static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

    // return an authToken
    public Result login() {

        JsonNode json = request().body().asJson();

        String emailAddress = json.findPath("email").textValue();
        String password = json.findPath("password").textValue();

        User user = User.findByEmailAddressAndPassword(emailAddress, password);

        if (user == null) {
            return unauthorized();
        } else {

            String authToken = user.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(Http.Cookie.builder(AUTH_TOKEN, authToken).withSecure(ctx().request().secure()).build());

            return ok(authTokenJson);

        }
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
