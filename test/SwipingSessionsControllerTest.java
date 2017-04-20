
package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import models.Activity;
import models.SwipingSession;
import org.junit.Test;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Http;
import testResources.BaseTest;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class SwipingSessionsControllerTest extends BaseTest {

    /*
     * Utility methods.
     */

    private Result makeAuthenticatedGetRequest(String authToken, String email1, String email2) {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.SwipingSessionsController.getSwipingSession(email1, email2));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Result makeGetRequestWithCorrectEmails() {
        return makeGetRequest(user1Email, user2Email);
    }

    private Result makeGetRequest(String email1, String email2) {

        String authToken = user1.createToken();
        return makeAuthenticatedGetRequest(authToken, email1, email2);

    }

    private Result makeAuthenticatedPostRequest(String authToken, String email1, String email2) {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.SwipingSessionsController.createSwipingSession(email1, email2));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Result makePostRequest(String email1, String email2) {

        String authToken = user1.createToken();
        return makeAuthenticatedPostRequest(authToken, email1, email2);

    }

    private Result makePostRequestWithCorrectEmails() {
        return makePostRequest(user1Email, user2Email);
    }

    private Result makeAuthenticatedPutRequest(String authToken, long swipingSessionId, String email, String activities) {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.SwipingSessionsController.chooseActivities(
                swipingSessionId, email, activities
        ));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Result makePutRequest(long swipingSessionId, String email, String activities) {

        String authToken = user1.createToken();
        return makeAuthenticatedPutRequest(authToken, swipingSessionId, email, activities);

    }

    private Result makePutRequestWithCorrectEmail(long swipingSessionId, String activities) {
        return makePutRequest(swipingSessionId, user1Email, activities);
    }

    /*
     * Test GET.
     */

    @Test
    public void getSwipingSession() {

        Result result = makeGetRequestWithCorrectEmails();
        assertEquals(OK, result.status());

        JsonNode json = Json.parse(contentAsString(result));
        for (JsonNode swipingSession : json) {

            assertNotNull(swipingSession.get("id"));
            assertNotNull(swipingSession.get("initializationDate"));

        }
    }

    @Test
    public void getSwipingSessionWhenNotLoggedIn() {

        Result result = route(fakeRequest(controllers.routes.SwipingSessionsController.getSwipingSession(user1Email,
                user2Email)));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void getSwipingSessionOnNonExistantInitializer() {
        
        Result result = makeGetRequest("nonexistant@demo.com", user2Email);
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNonExistantBuddy() {
        
        Result result = makeGetRequest(user1Email, "nonexistant@demo.com");
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNullInitializer() {
        
        Result result = makeGetRequest(null, user2Email);
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNullBuddy() {
        
        Result result = makeGetRequest(user1Email, null);
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNonExistantSwipingSession() {

        Result result = makeGetRequest(user2Email, user1Email);
        assertEquals(NOT_FOUND, result.status());
        
    }

    /*
     * Test POST.
     */

    @Test
    public void testInitiateSwipingSessionWhenNotLoggedIn() {

        Result result = route(fakeRequest(controllers.routes.SwipingSessionsController.createSwipingSession(user1Email,
                user2Email)));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void testInitiateSwipingSessionOnIncorrectEmailsButCorrectSession() {

        Result result = makePostRequest("nonexistant1@demo.com", "nonexistant2@demo.com");
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testInitiateSwipingSessionReturnsOkOnCorrectCredentials() {

        Result result = makePostRequestWithCorrectEmails();
        assertEquals(OK, result.status());

    }

    @Test
    public void testInitiateSwipingSessionCreatesSwipingSession() {

        List<SwipingSession> before = SwipingSession.findByEmail(user1Email, user2Email);

        makePostRequestWithCorrectEmails();

        List<SwipingSession> after = SwipingSession.findByEmail(user1Email, user2Email);

        assertTrue(after.size() == (before.size() + 1));

    }

    @Test
    public void testInitiateSwipingSessionReturnsNonEmptyList() {

        Result result = makePostRequestWithCorrectEmails();

        JsonNode json = Json.parse(contentAsString(result));
        assertNotNull(json);

        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();

        try {

            List<String> activities = mapper.readValue(json.get("activities").toString(),
                    typeFactory.constructCollectionType(List.class, String.class));

            assertNotNull(activities);
            assertTrue(activities.size() > 0);

        } catch (IOException e) {
            fail("IOException when creating activity list from response");
        }
    }

    @Test
    public void testCannotInitiateSwipingSessionWithOneself() {
        // Implement this.
    }

    /*
     * Test PUT.
     */

    @Test
    public void testChooseActivitiesWhenNotLoggedIn() {

        Result result = route(fakeRequest(controllers.routes.SwipingSessionsController.chooseActivities(2, "email@email.com", "[]")));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void testChooseActivitiesOnIncorrectEmailsButCorrectSession() {

        Result result = makePutRequest(1,"nonexistant1@demo.com", "[]");
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testChooseActivitiesOnCorrectCredentials() {

        Result result = makePutRequestWithCorrectEmail(1, "[]");
        assertEquals(OK, result.status());

    }

    private boolean compareListAndJsonList(List<Activity> activityList, JsonNode jsonList) {

        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();

        try {

            List<Activity> activitiesFromJson = mapper.readValue(jsonList.toString(),
                    typeFactory.constructCollectionType(List.class, Activity.class));

            for (int i = 0; i < activitiesFromJson.size(); i++) {

                if (!(activitiesFromJson.get(i).name.equals(activityList.get(i).name))) {
                    return false;
                }
            }

        } catch (IOException e) {
            return false;
        }
        return true;

    }

    @Test
    public void testChooseActivitiesUpdatesSwipingSession() {

        Result postResult = makePostRequestWithCorrectEmails();
        JsonNode json = Json.parse(contentAsString(postResult));

        try {

            long swipingSessionId = Long.parseLong(json.get("swipingSessionId").toString());
            Result putResult = makePutRequestWithCorrectEmail(swipingSessionId, json.get("activities").toString());

            assertEquals(OK, putResult.status());

            SwipingSession swipingSession = SwipingSession.findById(swipingSessionId);
            //assertTrue(compareListAndJsonList(swipingSession.initiatorActivities, json.get("activities")));
            assertTrue(compareListAndJsonList(swipingSession.getChosenActivities(user1Email), json.get("activities")));

        } catch (NumberFormatException e) {
            fail("NumberFormatException when getting swiping session id");
        }
    }

    @Test
    public void testCannotChooseActivitiesThatWereNotSent() {
        // Implement this.
    }

    @Test
    public void testCannotChooseActivitiesThatDoNotExist() {
        // Implement this.
    }
}
