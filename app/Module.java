import com.google.inject.AbstractModule;
import repositories.accommodation.AccommodationRepository;
import repositories.accommodation.AccommodationStorage;
import repositories.facebookData.FacebookDataRepository;
import repositories.facebookData.FacebookDataStorage;
import repositories.interests.InterestStorage;
import repositories.interests.InterestsRepository;
import repositories.users.UserStorage;
import repositories.users.UsersRepository;

/**
 * @author Simon Olofsson
 */
public class Module extends AbstractModule {

    @Override
    protected void configure() {

        bind(AccommodationStorage.class).to(AccommodationRepository.class);
        bind(FacebookDataStorage.class).to(FacebookDataRepository.class);
        bind(InterestStorage.class).to(InterestsRepository.class);
        bind(UserStorage.class).to(UsersRepository.class);

    }
}
