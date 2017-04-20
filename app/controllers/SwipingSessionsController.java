package controllers;

import akka.actor.dsl.Creators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import models.Activity;
import play.Logger;
import play.mvc.*;
import models.SwipingSession;
import models.User;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.util.ArrayList;
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

        List<SwipingSession> swipingSessions = SwipingSession.findByEmail(initiatorEmail, buddyEmail);

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

        SwipingSession swipingSession = new SwipingSession(initiatorEmail, buddyEmail);
        swipingSession.save();

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode json = mapper.createObjectNode();
        json.put("swipingSessionId", swipingSession.id);

        ArrayNode array = json.putArray("activities");
        array.add("Moderna Museet");

        return ok(json);

    }

    public Result chooseActivities(long swipingSessionId, String email, String activities) {

        if (User.findByEmailAddress(email) == null) {
            return badRequest();
        }

        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();

        try {

            List<String> activityNames = mapper.readValue(activities,
                    typeFactory.constructCollectionType(List.class, String.class));

            List<Activity> parsedActivities = new ArrayList<>();

            activityNames.forEach(activityName -> parsedActivities.add(Activity.findByName(activityName)));

            SwipingSession swipingSession = SwipingSession.findById(swipingSessionId);

            long userId = User.findByEmailAddress(email).id;

            if (userId == swipingSession.initiator.id) {
                swipingSession.initiatorActivities = parsedActivities;
            } else if(userId == swipingSession.buddy.id) {
                swipingSession.buddyActivities = parsedActivities;
            } else {
                return badRequest("The email address passed does not match any of the participants in the swiping session.");
            }

            try {
                swipingSession.save();
            } catch(PersistenceException pe) {
                return badRequest("Got persistence exception - did you pass an activity that does not exist?");
            }

            return ok();

        } catch (IOException e) {
            return badRequest("Malformed list of activities.");
        }
    }
}
