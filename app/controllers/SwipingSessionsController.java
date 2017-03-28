package controllers;

import play.mvc.*;
import models.SwipingSession;

import static play.libs.Json.toJson;

@Security.Authenticated(Secured.class)
public class SwipingSessionsController extends Controller {

    public Result getSwipingSession(String initiatorEmail, String buddyEmail) {
        return ok(toJson(SwipingSession.find(initiatorEmail, buddyEmail)));
    }
}
