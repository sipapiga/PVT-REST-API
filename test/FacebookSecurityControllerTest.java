package controllers;

import org.junit.Test;
import play.mvc.Result;
import testResources.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

public class FacebookSecurityControllerTest extends BaseTest {

    @Test
    public void testLogin() {

        if (facebookToken != null) {

            Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, facebookToken));
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

            Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, facebookToken));
            assertEquals(OK, result.status());

            result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, facebookToken));
            assertEquals(OK, result.status());

        } else {
            fail("No valid Facebook token detected. " +
                    "Please specify one in 'conf/application.secrets.conf' under the key 'facebookToken'." +
                    "Valid access tokens can be obtained from https://developers.facebook.com/tools/explorer");
        }
    }

    @Test
    public void testLoginOnEmptyAccessToken() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, ""));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void testLoginOnInvalidAccessToken() {

        Result result = route(fakeRequest(controllers.routes.FacebookSecurityController.login()).header(FacebookSecurityController.AUTH_TOKEN_HEADER, "invalid"));
        assertEquals(BAD_REQUEST, result.status());

    }
}
