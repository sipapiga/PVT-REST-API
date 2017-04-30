package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author Simon Olofsson
 */
public class HomeController extends Controller {

    public Result index() {
        return redirect("http://docs.rentsthlm.apiary.io");
    }
}
