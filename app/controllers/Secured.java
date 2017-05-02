package controllers;

import models.user.User;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import java.util.function.Predicate;

/**
 * Authenticator class to be used with @Security.Authenticated to
 * secure endpoints needing authentication to be accessed.
 *
 * @author James Ward, https://github.com/jamesward/play-rest-security.
 */
public class Secured extends Security.Authenticator {

    String evaluateUserPrivileges(Context ctx, Predicate<User> predicate) {

        String[] authTokenHeaderValues = ctx.request().headers().get(SecurityController.AUTH_TOKEN_HEADER);

        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {

            User user = User.findByAuthToken(authTokenHeaderValues[0]);

            if (predicate.test(user)) {

                ctx.args.put("user", user);
                return user.getEmailAddress();
            }
        }
        return null;

    }

    @Override
    public String getUsername(Context ctx) {
        return evaluateUserPrivileges(ctx, (user) -> user != null);
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return unauthorized();
    }
}
