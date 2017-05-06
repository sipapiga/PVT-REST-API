package testResources.mocks;

import models.Interest;
import models.accommodation.Accommodation;
import models.user.Tenant;
import repositories.interests.InterestStorage;
import scala.Option;

import java.util.List;

/**
 * @author Simon Olofsson
 */
public class MockInterestRepository implements InterestStorage {

    @Override
    public Interest create(Tenant tenant, Accommodation accommodation) {
        return null;
    }

    @Override
    public List<Interest> findInterests(Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) {
        return null;
    }

    @Override
    public Interest findInterest(long tenantId, long accommodationId) {
        return null;
    }

    @Override
    public void save(Interest interest) {

    }

    @Override
    public void delete(Interest interest) {

    }
}
