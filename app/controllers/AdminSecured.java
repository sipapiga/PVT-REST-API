package controllers;

import models.Authorization;
import play.mvc.Http.Context;

/**
 * @author Simon Olofsson
 */
public class AdminSecured extends Secured {

    @Override
    public String getUsername(Context ctx) {
        return evaluateUserPrivileges(ctx, (user) -> user != null && user.authorization == Authorization.ADMIN);
    }
}
