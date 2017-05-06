package repositories.interests;

import models.Interest;
import models.accommodation.Accommodation;
import models.user.Tenant;
import scala.Option;

import java.util.List;

/**
 * @author Simon Olofsson
 */
public interface InterestStorage {

    Interest create(Tenant tenant, Accommodation accommodation);

    List<Interest> findInterests(Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual);

    Interest findInterest(long tenantId, long accommodationId);

    void save(Interest interest);

    void delete(Interest interest);

}
