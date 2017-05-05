package controllers;
import com.fasterxml.jackson.databind.JsonNode;
import jdk.nashorn.internal.ir.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import testResources.BaseTest;

import java.util.HashSet;


/**
 * Created by Enver on 2017-05-05.
 */
public class UsersControllerTest extends BaseTest {

    @Test
    public void testReturnsOKOnAuthorizedRequest(){

        Result result = makeAuthenticatedRequest();
        assertEquals(OK, result.status());


    }


    /*
    @Test
    public void testReturnTenantProfile(){
        Result result = makeAuthenticatedRequest();
        JsonNode responseBody = Json.parse(contentAsString(result));
        Logger.debug(responseBody.toString());

        String correct = "{" +
                "  \"emailAddress\": \"kalle@example.com\"," +
                "  \"fullName\": \"Kalle Blomkvist\"," +
                "  \"firstName\": \"Kalle\"," +
                "  \"lastName\": \"Blomkvist\"," +
                "  \"gender\": \"male\"," +
                "  \"id\": \"5\"," +
                "  \"description\": \"Hej, jag heter Kalle och behöver någonstans att bo.\"," +
                "  \"age\": \"23\"," +
                "  \"numberOfTenants\": \"1\"," +
                "  \"income\": \"18000.0\"," +
                "  \"maxRent\": \"5000\"," +
                "  \"occupation\": \"Karaktär i berättelse\"," +
                "  \"deposit\": \"8000.0\"," +
                "  \"rentalPeriod\": {\n" +
                "    \"start\": \"2017-05-01\"," +
                "    \"end\": \"2018-05-01\"" +
                "  }" +
                "}";

        JsonNode correctNode = Json.parse(correct);

        assertEquals(correctNode, responseBody);

    }
    */
    @Test
    public void testCannotMakeRequestIfNotTenant(){
        //Implement this, tobbe.

    }

    private Result makeAuthenticatedRequest() {

        String authToken = tenant1.createToken();
        Http.RequestBuilder fakeRequest = fakeRequest(routes.UsersController.returnTenantProfile());
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        return route(fakeRequest);
    }
}
