package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Interest;
import models.user.Renter;
import models.user.Tenant;
import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Option;
import services.interests.InterestsService;
import exceptions.OffsetOutOfRangeException;
import services.users.UsersService;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
@Security.Authenticated(Secured.class)
public class InterestsController extends Controller {

    private InterestsService interestsService;
    private UsersService usersService;

    @Inject
    public InterestsController(InterestsService interestsService, UsersService usersService) {

        this.interestsService = interestsService;
        this.usersService = usersService;

    }

    public Result get(Option<Integer> count, Option<Integer> offset,
                      Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) {

        if (!tenantId.isDefined() && !accommodationId.isDefined()) {
            return ResponseBuilder.buildBadRequest("At least one of tenantId and accommodationId has to be defined.", ResponseBuilder.MALFORMED_URI_PARAMETERS);
        }

        try {

            List<Interest> interests = interestsService.getSubset(count, offset, tenantId, accommodationId, mutual);
            return ResponseBuilder.buildOKList(interests);

        } catch(OffsetOutOfRangeException e) {
            return ResponseBuilder.buildBadRequest("The offset you have requested is larger than the number of results.", ResponseBuilder.OUT_OF_RANGE);
        }
    }

    public Result create() {

        JsonNode body = request().body().asJson();

        try {

            Tenant tenant = (Tenant) ctx().args.get("user");
            long accommodationId = body.findValue("accommodationId").asLong();

            usersService.addInterest(tenant, accommodationId);

            return noContent();

        } catch (IllegalArgumentException iae) {
            return ResponseBuilder.buildBadRequest(iae.getMessage(), ResponseBuilder.ILLEGAL_ARGUMENT);
        } catch (ClassCastException cce) {
            return ResponseBuilder.buildBadRequest("User must be a valid tenant.", ResponseBuilder.NO_SUCH_ENTITY);
        }
    }

    public Result setMutual(long tenantId, long accommodationId) {

        JsonNode body = request().body().asJson();

        try {

            Renter renter = (Renter) ctx().args.get("user");
            String mutual = body.findValue("mutual").textValue();

            if (!mutual.equals("true") && !mutual.equals("false")) {
                return ResponseBuilder.buildBadRequest("Attribute 'mutual' must be set to either 'true' or 'false'.", ResponseBuilder.ILLEGAL_ARGUMENT);
            }

            Interest interest = usersService.setMutualInterest(renter, tenantId, Boolean.parseBoolean(mutual));

            return ResponseBuilder.buildOKObject(interest);

        } catch (ClassCastException cce) {
            return ResponseBuilder.buildBadRequest("User must be a valid renter.", ResponseBuilder.NO_SUCH_ENTITY);
        }
    }

    public Result withdrawInterest(long tenantId, long accommodationId) {

        if (((User) ctx().args.get("user")).id != tenantId) {
            return ResponseBuilder.buildUnauthorizedRequest("Owner of token and owner of tenant id do not match. A user may only withdraw own interests.");
        }

        usersService.withdrawInterest(tenantId, accommodationId);

        return noContent();

    }
}
