package repositories.interests;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import models.Interest;
import models.accommodation.Accommodation;
import models.user.Tenant;
import scala.Option;
import utils.ResponseBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
public class InterestsRepository implements InterestStorage {

    @Override
    public Interest create(Tenant tenant, Accommodation accommodation) {

        Interest interest = new Interest(tenant, accommodation);
        save(interest);

        return interest;

    }

    @Override
    public List<Interest> findInterests(Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) {

        List<Function<ExpressionList<Interest>, ExpressionList<Interest>>> functions = Arrays.asList(

                exprList -> tenantId.isDefined() ? exprList.eq("tenant_id", tenantId.get()) : exprList,
                exprList -> accommodationId.isDefined() ? exprList.eq("interest_accommodation_id", accommodationId.get()) : exprList,
                exprList -> mutual.isDefined() ? exprList.eq("mutual", mutual.get()) : exprList

        );

        return Interest.filterBy(functions);
    }

    @Override
    public Interest findInterest(long tenantId, long accommodationId) {
        return Interest.findByTenantAndAccommodation(tenantId, accommodationId);
    }

    @Override
    public void save(Interest interest) {
        interest.save();
    }

    @Override
    public void delete(Interest interest) {

        if (interest == null) {
            throw new IllegalArgumentException("Interest to be deleted may not be null.");
        }

        interest.delete();

    }
}
