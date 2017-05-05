package controllers;

import models.user.Tenant;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.ResponseBuilder;

/**
 * Created by Enver on 2017-05-05.
 */

@Security.Authenticated(Secured.class)
public class UsersController extends Controller {

    public Result returnTenantProfile(){

        Tenant tenant = (Tenant)ctx().args.get("user");


        return ResponseBuilder.buildOKObject(tenant);


    }
}
