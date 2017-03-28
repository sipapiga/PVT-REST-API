package controllers;

import play.mvc.*;
import models.SwipingSession;
import models.User;

import static play.libs.Json.toJson;

@Security.Authenticated(Secured.class)
public class SwipingSessionsController extends Controller {

    public Result getSwipingSession(String initiatorEmail, String buddyEmail) {

        if (User.findByEmailAddress(initiatorEmail) == null || User.findByEmailAddress(buddyEmail) == null) {

            /*
             * The reasoning behind returning a 400 here, rather than a 404, is that a non-existant
             * user really must be the result of some client-side programming error. Firstly, since authentication
             * is required at least one of the users have to exist. Secondly, the only time this endpoint
             * should be accessed is if there has been some indication that a swiping session has been initialized
             * by another user. If you do not know what email address that user has you are using the api wrongly.
             */
            return badRequest();
        }

        SwipingSession swipingSession = SwipingSession.find(initiatorEmail, buddyEmail);

        if (swipingSession == null) {
            return notFound();
        }

        return ok(toJson(swipingSession));
    }
}
