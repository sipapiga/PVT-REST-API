package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;

/**
 * Endpoint controller for handling requests for authentication by username and
 * password.
 *
 * @author James Ward, https://github.com/jamesward/play-rest-security.
 */
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

        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }

        Login login = loginForm.get();

        User user = User.findByEmailAddressAndPassword(login.emailAddress, login.password);

        if (user == null) {
            return unauthorized();
        } else {

            String authToken = user.createToken();
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put(AUTH_TOKEN, authToken);
            response().setCookie(Http.Cookie.builder(AUTH_TOKEN, authToken).withSecure(ctx().request().secure()).build());

            Logger.debug("About to send OK");

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
