package testResources.mocks;

import models.accommodation.Accommodation;
import repositories.accommodation.AccommodationStorage;
import scala.Option;

import java.util.List;

/**
 * @author Simon Olofsson
 */
public class MockAccommodationRepository implements AccommodationStorage {

    @Override
    public Accommodation findById(long id) {
        return null;
    }

    @Override
    public List<Accommodation> findAccommodation(Option<Double> rent, Option<Double> size, Option<Boolean> smokingAllowed, Option<Boolean> animalsAllowed) {
        return null;
    }

}
