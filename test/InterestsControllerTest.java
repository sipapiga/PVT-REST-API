package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Interest;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.Renter;
import models.user.Tenant;
import org.junit.Test;
import play.Logger;
import play.api.libs.iteratee.RunQueue;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import scala.None;
import scala.Option;
import scala.tools.cmd.Opt;
import testResources.BaseTest;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.*;
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
    Option<Long> accommodationId = Option.apply(1L);
    Option<Boolean> mutual = Option.apply(false);

    /*
     * Utility methods.
     */

    private Result makeAuthenticatedRequest(Option<Integer> count, Option<Integer> offset, Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) {

        String authToken = renter1.createToken();

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.InterestsController.get(count, offset, tenantId, accommodationId, mutual));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Accommodation createRenterAndAccommodation() {

        Renter renter2 = new Renter("eva@example.com", "password", "Eva Hellman", "Tja! Här vare' boende.", 52);
        renter2.save();

        Address address = new Address("Hägerstensvägen", 108, "Mälarhöjden", 100, 100);
        address.save();

        Accommodation renter2Accommodation = renter2.createAccommodation(5500, 25, 1, 6000,
                false, false, true, true, "Schyrrebyrre", address);

        return renter2Accommodation;

    }

    private Result makePostRequest(String authToken, long tenantId, long accommodationId) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("tenantId", tenantId);
        objectNode.put("accommodationId", accommodationId);

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.InterestsController.create());
        fakeRequest.bodyJson(objectNode);
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);

    }

    private Result createRenterAndAccommodationAndMakePostRequest() {

        Accommodation renter2Accommodation = createRenterAndAccommodation();

        String authToken = tenant1.createToken();

        return makePostRequest(authToken, tenant1.id, renter2Accommodation.id);

    }

    /*
     * GET
     */

    @Test
    public void returns401OnUnauthorizedGetRequest() {

        Result result = route(fakeRequest(controllers.routes.InterestsController.get(count, offset, tenantId, accommodationId, mutual)));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void returns200OnAuthorizedGetRequest() {

        Result result = makeAuthenticatedRequest(count, offset, tenantId, accommodationId, mutual);
        assertEquals(OK, result.status());

    }

    @Test
    public void returnsAllInterestsOnAuthorizedGet() {

        Result result = makeAuthenticatedRequest(count, offset, tenantId, accommodationId, mutual);
        JsonNode responseBody = Json.parse(contentAsString(result));

        assertTrue(responseBody.size() > 0);

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

    @Test
    public void returnsOnlySpecifiedRangeOnGet() {

        Accommodation renter2Accommodation = createRenterAndAccommodation();

        tenant1.addInterest(renter2Accommodation);

        Option<Integer> count = Option.apply(1);
        Option<Integer> offset = Option.apply(0);
        Option<Long> tenantId = Option.apply(tenant1.id);
        Option<Long> accommodationId = Option.empty();
        Option<Boolean> mutual = Option.empty();

        String authToken = tenant1.createToken();

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.InterestsController.get(count, offset, tenantId, accommodationId, mutual));
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        Result result = route(fakeRequest);
        JsonNode responseBody = Json.parse(contentAsString(result));

        assertEquals(1, responseBody.size());

    }

    @Test
    public void cannotViewInterestsUserIsNotAuthorizedToView() {
        // Implement this
    }

    /*
     * POST
     */

    @Test
    public void authorizedPostReturnsNOCONTENT() {

        Result result = createRenterAndAccommodationAndMakePostRequest();
        assertEquals(NO_CONTENT, result.status());

    }

    @Test
    public void authorizedPostSavesInterest() {

        Tenant tenant2 = new Tenant("jonte@example.com", "password", "Jonte Jontesson",
                "Jag heter Jonte", 25, 1, 5000, 18000, "Jonte!", 8000);

        Result result = makePostRequest(tenant2.createToken(), tenant2.id, renter1Accommodation.id);
        assertNotNull(Interest.findByTenantAndAccommodation(tenant2.id, renter1Accommodation.id));

    }
}
