package controllers;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Interest;
import models.accommodation.Accommodation;
import models.user.Renter;
import models.user.Tenant;
import models.user.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Option;
import utils.ResponseBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
@Security.Authenticated(Secured.class)
public class InterestsController extends Controller {

    public Result get(Option<Integer> count, Option<Integer> offset,
                                     Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) {

        if (!tenantId.isDefined() && !accommodationId.isDefined()) {
            return ResponseBuilder.buildBadRequest("At least one of tenantId and accommodationId has to be defined.", ResponseBuilder.MALFORMED_URI_PARAMETERS);
        }

        List<Function<ExpressionList<Interest>, ExpressionList<Interest>>> functions = Arrays.asList(

            exprList -> tenantId.isDefined()        ? exprList.eq("tenant_id", tenantId.get())                        : exprList,
            exprList -> accommodationId.isDefined() ? exprList.eq("interest_accommodation_id", accommodationId.get()) : exprList,
            exprList -> mutual.isDefined()          ? exprList.eq("mutual", mutual.get())                             : exprList

        );

        List<Interest> interests = Interest.filterBy(functions);

        int evaluatedOffset = offset.isDefined() ? offset.get() : 0;
        int evaluatedCount = count.isDefined() && ((count.get() + evaluatedOffset) < interests.size()) ? count.get() : interests.size();

        if (evaluatedOffset > interests.size()) {
            return ResponseBuilder.buildBadRequest("The offset you have requested is larger than the number of results.", ResponseBuilder.OUT_OF_RANGE);
        }

        interests = interests.subList(evaluatedOffset, evaluatedCount);

        return ResponseBuilder.buildOKList(interests);

    }

    public Result create() {

        JsonNode body = request().body().asJson();

        try {

            Tenant tenant = (Tenant) ctx().args.get("user");
            long accommodationId = body.findValue("accommodationId").asLong();

            tenant.addInterest(Accommodation.findById(accommodationId));

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

            Interest interest = Interest.findByTenantAndAccommodation(tenantId, accommodationId);
            interest.setMutual(Boolean.parseBoolean(mutual));

            return ResponseBuilder.buildOKObject(interest);

        } catch (ClassCastException cce) {
            return ResponseBuilder.buildBadRequest("User must be a valid renter.", ResponseBuilder.NO_SUCH_ENTITY);
        }
    }

    public Result withdrawInterest(long tenantId, long accommodationId) {

        if (((User) ctx().args.get("user")).id != tenantId) {
            return ResponseBuilder.buildUnauthorizedRequest("Owner of token and owner of tenant id do not match. A user may only withdraw own interests.");
        }

        Interest interest = Interest.findByTenantAndAccommodation(tenantId, accommodationId);
        interest.delete();

        return noContent();

    }
}
