package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Activity;
import play.mvc.*;
import models.SwipingSession;
import models.User;
import utils.ActivityGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Endpoint controller for getting, creating and updating swiping sessions.
 *
 * @author Simon Olofsson
 */
@Security.Authenticated(Secured.class)
public class SwipingSessionsController extends Controller {

    /**
     * Method for getting swiping sessions where all of the users indicated
     * by the passed email addresses are or have participated.
     *
     * @param emailAddresses a json array of email addresses.
     * @return 200 OK if the array passed is valid and there is at least one
     * swiping session associated with all those emails, 400 BAD REQUEST if
     * the list of emails is malformed and 404 NOT FOUND if there is no swiping
     * session associated with all of the email addresses passed.
     */
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

    /**
     * Method for initiating a new swiping session and generating a list of
     * activities to choose from. Initiates a swiping session with all of the
     * users indicated by the email addresses passed.
     *
     * @param emailAddresses a json array of email addresses.
     * @return 200 OK if all of the email addresses passed are valid and can be
     * connected to existing users, 400 BAD REQUEST if the list is either
     * malformed or contains email addresses that cannot be associated with
     * existing users. Will also return a json array of activities to choose
     * from and the id of the swiping session created.
     */
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

    /**
     * Method for recording a choice of activities on behalf of the user with
     * the email address passed. Will update the database record for the
     * indicated swiping session with the activities passed.
     *
     * @param swipingSessionId the id of the swiping session, provided on
     *                         its creation.
     * @param email the email address of the user who made the choice.
     * @param activities a json array of activities, indicating the users
     *                   choice.
     * @return 200 OK if the swiping session exists and the email address can
     * be connected to an existing user, 400 BAD REQUEST if the user indicated
     * or any of the chosen activities does/do not exist or if the list of
     * activities is in any way malformed.
     */
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
