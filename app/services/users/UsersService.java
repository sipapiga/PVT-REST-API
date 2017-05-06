package services.users;

import com.fasterxml.jackson.databind.JsonNode;
import models.Interest;
import models.accommodation.Accommodation;
import models.user.Renter;
import models.user.Tenant;
import models.user.User;
import repositories.accommodation.AccommodationRepository;
import repositories.accommodation.AccommodationStorage;
import repositories.facebookData.FacebookDataRepository;
import repositories.facebookData.FacebookDataStorage;
import repositories.interests.InterestStorage;
import repositories.interests.InterestsRepository;
import repositories.users.UserStorage;
import repositories.users.UsersRepository;

import javax.inject.Inject;

/**
 * @author Simon Olofsson
 */
public class UsersService {

    private UserStorage usersRepository;
    private AccommodationStorage accommodationRepository;
    private InterestStorage interestsRepository;
    private FacebookDataStorage facebookDataRepository;

    @Inject
    public UsersService(UserStorage usersRepository,
                        AccommodationStorage accommodationRepository,
                        InterestStorage interestsRepository,
                        FacebookDataStorage facebookDataRepository) {

        this.usersRepository = usersRepository;
        this.accommodationRepository = accommodationRepository;
        this.interestsRepository = interestsRepository;
        this.facebookDataRepository = facebookDataRepository;

    }

    public void addInterest(Tenant tenant, long accommodationId) {

        Accommodation accommodation = accommodationRepository.findById(accommodationId);
        Interest interest = interestsRepository.create(tenant, accommodation);

        tenant.addInterest(interest);

    }

    public Interest setMutualInterest(Renter renter, long tenantId, boolean mutual) {

        Interest interest = interestsRepository.findInterest(tenantId, renter.accommodation.id);
        interest.mutual = mutual;

        interestsRepository.save(interest);

        return interest;

    }

    public void withdrawInterest(long tenantId, long accommodationId) {

        Interest interest = interestsRepository.findInterest(tenantId, accommodationId);
        interestsRepository.delete(interest);

    }

    public User createFromFacebookData(JsonNode facebookData) {

        String email = facebookData.findValue("email").textValue();
        User user = usersRepository.findByEmailAddress(email);

        if (user == null) {
            user = new User();
        }

        String facebookUserId = facebookData.findValue("id").textValue();
        user.facebookData = facebookDataRepository.findByFacebookUserId(facebookUserId);
        usersRepository.save(user);

        return user;

    }

    public String getToken(User user) {

        String userToken = user.createToken();
        usersRepository.save(user);

        return userToken;

    }

    public void deleteToken(User user) {

        user.deleteAuthToken();
        user.save();

    }

    public User findByEmailAddressAndPassword(String emailAddress, String password) {
        return usersRepository.findByEmailAddressAndPassword(emailAddress, password);
    }
}
