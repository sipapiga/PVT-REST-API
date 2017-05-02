package controllers;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Interest;
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
            return ResponseBuilder.buildBadRequest("At least one of tenantId and accommodationId has to be defined", ResponseBuilder.MALFORMED_URI_PARAMETERS);
        }

        List<Function<ExpressionList<Interest>, ExpressionList<Interest>>> functions = Arrays.asList(

                exprList -> tenantId.isDefined() ? exprList.eq("tenant_id", tenantId.get()) : exprList,
                exprList -> accommodationId.isDefined() ? exprList.eq("interest_accommodation_id", accommodationId.get()) : exprList,
                exprList -> mutual.isDefined() ? exprList.eq("mutual", mutual.get()) : exprList

        );

        List<Interest> interests = Interest.filterBy(functions);

        interests = interests.subList(offset.isDefined() ? offset.get() : 0,
                count.isDefined() && count.get() < interests.size() ? count.get() : interests.size());

        return ResponseBuilder.buildOKList(interests);

    }
}
