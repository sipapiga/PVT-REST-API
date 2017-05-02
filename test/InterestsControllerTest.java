import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.SecurityController;
import controllers.routes;
import org.junit.Test;
import play.api.libs.iteratee.RunQueue;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import scala.None;
import scala.Option;
import scala.tools.cmd.Opt;
import testResources.BaseTest;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

/**
 * @author Simon Olofsson
 */
public class InterestsControllerTest extends BaseTest {

    Option<Integer> count = Option.apply(10);
    Option<Integer> offset = Option.apply(0);
    Option<Long> tenantId = Option.empty();
    Option<Long> accommodationId = Option.apply(1l);
    Option<Boolean> mutual = Option.apply(false);

    private Result makeAuthenticatedRequest(Option<Integer> count, Option<Integer> offset, Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) {

        String authToken = renter1.createToken();

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.InterestsController.get(count, offset, tenantId, accommodationId, mutual));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    @Test
    public void returns401OnUnauthorizedRequest() {

        Result result = route(fakeRequest(controllers.routes.InterestsController.get(count, offset, tenantId, accommodationId, mutual)));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void returns200OnAuthorizedRequest() {

        Result result = makeAuthenticatedRequest(count, offset, tenantId, accommodationId, mutual);
        assertEquals(OK, result.status());

    }

    @Test
    public void returnsAllInterestsOnAuthorizedGet() {

        Result result = makeAuthenticatedRequest(count, offset, tenantId, accommodationId, mutual);
        JsonNode responseBody = Json.parse(contentAsString(result));

        for (JsonNode interest : responseBody) {

            assertNotNull(interest.findValue("tenantId"));

            try {

                int tenantId = Integer.parseInt(interest.findValue("tenantId").asText());

            } catch (NumberFormatException e) {
                fail("NumberFormatException when parsing tenantId");
            }

            assertNotNull(interest.findValue("accommodationId"));

            try {

                int accommodationId = Integer.parseInt(interest.findValue("accommodationId").asText());

            } catch (NumberFormatException e) {
                fail("NumberFormatException when parsing accommodationId");
            }

            assertNotNull(interest.findValue("mutual"));
            assertTrue(interest.findValue("mutual").asText().equals("true") || interest.findValue("mutual").asText().equals("false"));

        }
    }
}
