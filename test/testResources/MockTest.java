package testResources;

import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import repositories.accommodation.AccommodationStorage;
import repositories.facebookData.FacebookDataStorage;
import repositories.interests.InterestStorage;
import repositories.users.UserStorage;
import testResources.mocks.MockAccommodationRepository;
import testResources.mocks.MockFacebookDataRepository;
import testResources.mocks.MockInterestRepository;
import testResources.mocks.MockUserRepository;

/**
 * @author Simon Olofsson
 */
public class MockTest extends BaseTest {

    /*@Override
    protected Application provideApplication() {

        return new GuiceApplicationBuilder().overrides(binder -> {

            binder.bind(AccommodationStorage.class).to(MockAccommodationRepository.class);
            binder.bind(FacebookDataStorage.class).to(MockFacebookDataRepository.class);
            binder.bind(InterestStorage.class).to(MockInterestRepository.class);
            binder.bind(UserStorage.class).to(MockUserRepository.class);

        }).in(Mode.TEST).build();
    }*/
}
