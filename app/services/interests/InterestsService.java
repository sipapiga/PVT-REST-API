package services.interests;

import models.Interest;
import repositories.interests.InterestStorage;
import repositories.interests.InterestsRepository;
import scala.Option;
import exceptions.OffsetOutOfRangeException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class InterestsService {

    private final InterestStorage interestsRepository;

    @Inject
    public InterestsService(InterestStorage interestsRepository) {
        this.interestsRepository = interestsRepository;
    }

    public List<Interest> getSubset(Option<Integer> count, Option<Integer> offset,
                                    Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) throws OffsetOutOfRangeException {

        List<Interest> interests = interestsRepository.findInterests(tenantId, accommodationId, mutual);

        int evaluatedOffset = offset.isDefined() ? offset.get() : 0;
        int evaluatedCount = count.isDefined() && ((count.get() + evaluatedOffset) < interests.size()) ? count.get() : interests.size();

        if (evaluatedOffset > interests.size()) {
            throw new OffsetOutOfRangeException("The offset you have requested is larger than the number of results.");
        }

        return interests.subList(evaluatedOffset, evaluatedCount);
    }

    public boolean deleteInterest(long tenantId, long accommodationId) {

        Interest interest = interestsRepository.findInterest(tenantId, accommodationId);

        if (interest == null) {
            return false;
        }

        interestsRepository.delete(interest);

        return true;
    }
}
