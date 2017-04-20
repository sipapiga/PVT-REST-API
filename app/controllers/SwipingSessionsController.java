package controllers;

import akka.actor.dsl.Creators;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
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

    private Result buildBadRequestResponse(ObjectMapper mapper, String message) {

        ObjectNode responseBody = mapper.createObjectNode();

        ObjectNode error = responseBody.putObject("error");
        error.put("message", message);

        return badRequest(responseBody);

    }

    public Result chooseActivities(long swipingSessionId, String email, String activities) {

        if (User.findByEmailAddress(email) == null) {
            return badRequest();
        }

        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode jsonActivities = mapper.readTree(activities);
            List<Activity> parsedActivities = new ArrayList<>();

            jsonActivities.forEach(activityName -> parsedActivities.add(Activity.findByName(activityName.asText())));

            SwipingSession swipingSession = SwipingSession.findById(swipingSessionId);

            /*long userId = User.findByEmailAddress(email).id;

            if (userId == swipingSession.initiator.id) {
                swipingSession.initiatorActivities = parsedActivities;
            } else if(userId == swipingSession.buddy.id) {
                swipingSession.buddyActivities = parsedActivities;
            } else {
                return buildBadRequestResponse(mapper,
                        "The email address passed does not match any of the participants in the swiping session.");
            }*/

            swipingSession.setUserActivityChoice(email, parsedActivities);

            try {
                swipingSession.save();
            } catch(PersistenceException pe) {
                return buildBadRequestResponse(mapper,"Got persistence exception - did you pass an activity that does not exist?");
            }

            return ok();

        } catch (IOException e) {
            return buildBadRequestResponse(mapper, "Malformed list of activities");
        }
    }
}
