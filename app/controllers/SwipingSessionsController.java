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
import utils.ActivityGenerator;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Security.Authenticated(Secured.class)
public class SwipingSessionsController extends Controller {

    public Result getSwipingSession(String emailAddresses) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode jsonEmails = mapper.readTree(emailAddresses);
            List<String> emailList = new ArrayList<>();

            jsonEmails.forEach(emailAddress -> emailList.add(emailAddress.asText()));

            List<SwipingSession> swipingSessions = SwipingSession.findByEmail(emailList);

            if (swipingSessions == null || swipingSessions.isEmpty()) {
                return notFound();
            }

            ArrayNode json = mapper.valueToTree(swipingSessions);

            return ok(json);

        } catch (IOException e) {
            return buildBadRequestResponse(mapper, "Malformed list of emails");
        }

    }

    public Result createSwipingSession(String emailAddresses) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode jsonEmails = mapper.readTree(emailAddresses);
            Set<User> participatingUsers = new HashSet<>();

            jsonEmails.forEach(emailAddress -> participatingUsers.add(User.findByEmailAddress(emailAddress.asText())));

            SwipingSession swipingSession = new SwipingSession(participatingUsers);

            try {
                swipingSession.save();
            } catch (RuntimeException re) {
                return buildBadRequestResponse(mapper, "Invalid email.");
            }

            ObjectNode json = mapper.createObjectNode();
            json.put("swipingSessionId", swipingSession.id);

            ActivityGenerator.generateActivitiesAsArrayNode(json);

            return ok(json);

        } catch (IOException e) {
            return buildBadRequestResponse(mapper, "Malformed list of emails");
        }
    }

    private Result buildBadRequestResponse(ObjectMapper mapper, String message) {

        ObjectNode responseBody = mapper.createObjectNode();

        ObjectNode error = responseBody.putObject("error");
        error.put("message", message);

        return badRequest(responseBody);

    }

    public Result chooseActivities(long swipingSessionId, String email, String activities) {

        ObjectMapper mapper = new ObjectMapper();

        if (User.findByEmailAddress(email) == null) {
            return buildBadRequestResponse(mapper, "No user with that email address found.");
        }

        try {

            JsonNode jsonActivities = mapper.readTree(activities);
            List<Activity> parsedActivities = new ArrayList<>();

            jsonActivities.forEach(activityName -> parsedActivities.add(Activity.findByName(activityName.asText())));

            SwipingSession swipingSession = SwipingSession.findById(swipingSessionId);

            try {

                swipingSession.setUserActivityChoice(email, parsedActivities);
                swipingSession.save();

            } catch(RuntimeException re) {
                return buildBadRequestResponse(mapper,"Got exception - did you pass an activity that does not exist?");
            }

            return ok();

        } catch (IOException e) {
            return buildBadRequestResponse(mapper, "Malformed list of activities.");
        }
    }
}
