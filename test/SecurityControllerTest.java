/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.*;

import play.libs.Json;
import play.mvc.Result;
import testResources.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class SecurityControllerTest extends BaseTest {

    private ObjectNode loginJson;

    private void setup() {
        loginJson = Json.newObject();
    }

    @Test
    public void login() {

        setup();

        loginJson.put("emailAddress", user1Email);
        loginJson.put("password", user1Password);

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));

        assertEquals(OK, result.status());

        JsonNode json = Json.parse(contentAsString(result));
        assertNotNull(json.get("authToken"));

    }

    @Test
    public void loginWithBadPassword() {

        setup();
        loginJson.put("emailAddress", user1Email);
        loginJson.put("password", user1Password.substring(1));

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void loginWithBadUsername() {

        setup();
        loginJson.put("emailAddress", user1Email.substring(1));
        loginJson.put("password", user1Password);

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(UNAUTHORIZED, result.status());
        
    }

    @Test
    public void loginWithDifferentCaseUsername() {

        setup();
        loginJson.put("emailAddress", user1Email.toUpperCase());
        loginJson.put("password", user1Password);

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(OK, result.status());

    }

    @Test
    public void loginWithNullPassword() {
        
        setup();
        loginJson.put("emailAddress", user1Email);

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void logout() {
        
        //String authToken = user1.createToken();
        String authToken = usersService.getToken(user1);

        Result result = route(fakeRequest(controllers.routes.SecurityController.logout()).header(SecurityController.AUTH_TOKEN_HEADER, authToken));
        assertEquals(SEE_OTHER, result.status());

    }
}



