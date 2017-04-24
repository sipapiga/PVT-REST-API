import controllers.ActivityGatheringController;
import controllers.SecurityController;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import testResources.BaseTest;

import static junit.framework.TestCase.assertEquals;
import static play.test.Helpers.*;

public class ActivityGatheringControllerTest extends BaseTest {

    @Test
    public void testUnauthorizedRequestOnNoCredentials() {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.ActivityGatheringController.gather());

        Result result = route(fakeRequest);
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void testUnauthorizedRequestOnOrdinaryUser() {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.ActivityGatheringController.gather());
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, user1.createToken());

        Result result = route(fakeRequest);
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void testAuthorizedRequestOnAdmin() {

        Http.RequestBuilder fakeRequest = fakeRequest(controllers.routes.ActivityGatheringController.gather());
        fakeRequest.header(SecurityController.AUTH_TOKEN_HEADER, admin.createToken());

        Result result = route(fakeRequest);
        assertEquals(OK, result.status());

    }
}
