
package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.junit.Before;

import play.libs.Json;
import play.mvc.Result;
import play.mvc.Http;
import play.test.WithApplication;
import utils.DemoData;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import play.Logger;

public class SwipingSessionsControllerTest extends WithApplication {

    DemoData demoData;

    /*@Before
    public void setup() {
        demoData = app.injector().instanceOf(DemoData.class);
    }

    private Result makeAuthenticatedRequest(String authToken, String email1, String email2) {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.SwipingSessionsController.getSwipingSession(email1, email2));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Result makeRequestWithCorrectEmails() {

        String authToken = demoData.user1.createToken();
        return makeAuthenticatedRequest(authToken, demoData.user1.getEmailAddress(), demoData.user2.getEmailAddress());

    }

    private Result makeRequest(String email1, String email2) {

        String authToken = demoData.user1.createToken();
        return makeAuthenticatedRequest(authToken, email1, email2);

    }

    @Test
    public void getSwipingSession() {

        Result result = makeRequestWithCorrectEmails();
        assertEquals(OK, result.status());

        JsonNode json = Json.parse(contentAsString(result));

        assertNotNull(json.get("id"));
        assertNotNull(json.get("initializationDate"));

    }

    @Test
    public void getSwipingSessionWhenNotLoggedIn() {

        Result result = route(fakeRequest(controllers.routes.SwipingSessionsController.getSwipingSession(demoData.user1.getEmailAddress(), demoData.user2.getEmailAddress())));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void getSwipingSessionOnNonExistantInitializer() {
        
        Result result = makeRequest("nonexistant@demo.com", demoData.user2.getEmailAddress());
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNonExistantBuddy() {
        
        Result result = makeRequest(demoData.user1.getEmailAddress(), "nonexistant@demo.com");
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNullInitializer() {
        
        Result result = makeRequest(null, demoData.user2.getEmailAddress());
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNullBuddy() {
        
        Result result = makeRequest(demoData.user1.getEmailAddress(), null);
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void getSwipingSessionOnNonExistantSwipingSession() {

        Result result = makeRequest(demoData.user2.getEmailAddress(), demoData.user1.getEmailAddress());
        assertEquals(NOT_FOUND, result.status());
        
    }*/
}
