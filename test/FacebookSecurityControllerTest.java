package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.mvc.Result;
import play.routing.Router;
import play.routing.RoutingDsl;
import play.server.Server;
import services.FacebookService;
import testResources.BaseTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Results.ok;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class FacebookSecurityControllerTest extends BaseTest {

    ObjectMapper mapper = new ObjectMapper();

    private JsonNode createRequestBody(String facebookToken) {

        ObjectNode bodyJson = mapper.createObjectNode();
        bodyJson.put(FacebookSecurityController.FACEBOOK_AUTH_TOKEN_BODY_FIELD, facebookToken);

        return bodyJson;

    }

    private JsonNode createBodyWithInvalidFacebookToken() {
        return createRequestBody("");
    }

    private JsonNode createBodyWithValidFacebookToken() {
        return createRequestBody(facebookToken);
    }
    /*@Test
    public void testLogin() {

        if (facebookToken != null) {

            Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).bodyJson(createBodyWithValidFacebookToken()));
            assertEquals(OK, result.status());

        } else {
            fail("No valid Facebook token detected. " +
                    "Please specify one in 'conf/application.secrets.conf' under the key 'facebookToken'." +
                    "Valid access tokens can be obtained from https://developers.facebook.com/tools/explorer");
        }
    }

    @Test
    public void testLoginSameUserTwice() {

        if (facebookToken != null) {

            Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).bodyJson(createBodyWithValidFacebookToken()));
            assertEquals(OK, result.status());

            result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).bodyJson(createBodyWithValidFacebookToken()));
            assertEquals(OK, result.status());

        } else {
            fail("No valid Facebook token detected. " +
                    "Please specify one in 'conf/application.secrets.conf' under the key 'facebookToken'." +
                    "Valid access tokens can be obtained from https://developers.facebook.com/tools/explorer");
        }
    }*/

    @Test
    public void testLoginOnEmptyAccessToken() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).bodyJson(createBodyWithInvalidFacebookToken()));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testLoginOnInvalidAccessToken() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).bodyJson(createBodyWithInvalidFacebookToken()));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testLoginOnNoRequestBody() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testLoginOnEmptyRequestBody() {

        ObjectNode bodyJson = mapper.createObjectNode();

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).bodyJson(bodyJson));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testLoginOnNullRequestBody() {

        ObjectNode bodyJson = null;

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).bodyJson(bodyJson));
        assertEquals(BAD_REQUEST, result.status());

    }
}
