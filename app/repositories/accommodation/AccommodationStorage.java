package repositories.accommodation;

import models.accommodation.Accommodation;
import scala.Option;

import java.util.List;

/**
 * @author Simon Olofsson
 */
public interface AccommodationStorage {

    Accommodation findById(long id);

    List<Accommodation> findAccommodation(final Option<Double> rent, final Option<Double> size,
                                          final Option<Boolean> smokingAllowed, final Option<Boolean> animalsAllowed);
}
