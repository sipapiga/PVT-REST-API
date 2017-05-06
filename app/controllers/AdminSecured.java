package controllers;

import models.user.Authorization;
import play.mvc.Http.Context;
import repositories.users.UsersRepository;

import javax.inject.Inject;

/**
 * @author Simon Olofsson
 */
public class AdminSecured extends Secured {

    @Inject
    public AdminSecured(UsersRepository usersRepository) {
        super(usersRepository);
    }

    @Override
    public String getUsername(Context ctx) {
        return evaluateUserPrivileges(ctx, (user) -> user != null && user.authorization == Authorization.ADMIN);
    }
}
