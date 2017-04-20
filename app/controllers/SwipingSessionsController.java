package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import play.Logger;
import play.mvc.*;
import models.SwipingSession;
import models.User;

import java.util.List;

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

        List<SwipingSession> swipingSessions = SwipingSession.find(initiatorEmail, buddyEmail);

        if (swipingSessions == null || swipingSessions.isEmpty()) {
            return notFound();
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode json = mapper.valueToTree(swipingSessions);

        return ok(json);

    }

    public Result createSwipingSession(String initiatorEmail, String buddyEmail) {

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

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        array.add("Moderna Museet");

        return ok(array);

    }

    public Result chooseActivities() {
        return null;
    }
}
