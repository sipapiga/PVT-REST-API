package controllers;

import org.junit.Test;
import play.mvc.Result;
import testResources.BaseTest;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class FacebookSecurityControllerTest extends BaseTest {

    @Test
    public void testLogin() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, facebookToken));
        assertEquals(OK, result.status());

    }

    @Test
    public void testLoginSameUserTwice() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, facebookToken));
        assertEquals(OK, result.status());

        result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, facebookToken));
        assertEquals(OK, result.status());

    }


    @Test
    public void testLoginOnEmptyAccessToken() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, ""));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testLoginOnInvalid() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, "invalid"));
        assertEquals(BAD_REQUEST, result.status());

    }
}
