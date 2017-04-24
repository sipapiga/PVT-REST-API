package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.ActivityGatherer;

/**
 * Endpoint controller for initiating gathering of data.
 *
 * @author Simon Olofsson
 */
@Security.Authenticated(AdminSecured.class)
public class ActivityGatheringController extends Controller {

    public Result gather() {

        ActivityGatherer gatherer = new ActivityGatherer();
        gatherer.gather();

        return ok();

    }
}
