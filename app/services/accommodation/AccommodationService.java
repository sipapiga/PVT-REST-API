package services.accommodation;

import exceptions.OffsetOutOfRangeException;
import models.accommodation.Accommodation;
import repositories.accommodation.AccommodationRepository;
import repositories.accommodation.AccommodationStorage;
import scala.Option;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class AccommodationService {

    private AccommodationStorage accommodationRepository;

    @Inject
    public AccommodationService(AccommodationStorage accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    public List<Accommodation> getSubset(final Option<Integer> count, final Option<Integer> offset,
                                         final Option<Double> rent, final Option<Double> size,
                                         final Option<Boolean> smokingAllowed, final Option<Boolean> animalsAllowed) throws OffsetOutOfRangeException {

        List<Accommodation> accommodation = accommodationRepository.findAccommodation(rent, size, smokingAllowed, animalsAllowed);

        int evaluatedOffset = offset.isDefined() ? offset.get() : 0;
        int evaluatedCount = count.isDefined() && ((count.get() + evaluatedOffset) < accommodation.size()) ? count.get() : accommodation.size();

        if (evaluatedOffset > accommodation.size()) {
            throw new OffsetOutOfRangeException("The offset you have requested is larger than the number of results.");
        }

        return accommodation.subList(evaluatedOffset, evaluatedCount);

    }
}
