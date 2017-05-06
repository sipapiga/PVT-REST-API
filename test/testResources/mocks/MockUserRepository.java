package testResources.mocks;

import models.user.Authorization;
import models.user.Renter;
import models.user.Tenant;
import models.user.User;
import play.Logger;
import repositories.users.UserStorage;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Olofsson
 */
public class MockUserRepository implements UserStorage {

    private Set<User> users = new HashSet<>();

    public MockUserRepository() {

        User user1 = new User("user1@demo.com", "password", "John Doe");
        users.add(user1);

        User user2 = new User("user2@demo.com", "password", "Jane Doe");
        users.add(user2);

        User admin = new User("admin@demo.com", "password", "Sven Svensson");
        admin.authorization = Authorization.ADMIN;
        users.add(admin);

        Renter renter1 = new Renter("anna@example.com", "password", "Anna Svensson", "Hej! Jag är en skön prick.", 35);
        users.add(renter1);

        Tenant tenant1 = new Tenant("kalle@example.com", "password", "Kalle Blomkvist",
                "Hej, Jag heter Kalle och behöver någonstans att bo.", 23, 1, 5000, 18000, "Karaktär i berättelse", 8000);
        users.add(tenant1);

    }

    @Override
    public User findByAuthToken(String authToken) {
        return users.stream().filter(user -> user.getAuthToken().equals(authToken)).findFirst().orElse(null);
    }

    @Override
    public Tenant findTenantById(long tenantId) {
       return (Tenant) users.stream().filter(user -> user.id == tenantId).findFirst().orElse(null);
    }

    @Override
    public User findByEmailAddress(String email) {
        return users.stream().filter(user -> user.getEmailAddress().equals(email)).findFirst().orElse(null);
    }

    @Override
    public User findByEmailAddressAndPassword(String emailAddress, String password) {
        return users.stream().filter(user -> user.getEmailAddress().equals(emailAddress) && user.getPassword().equals(password)).findFirst().orElse(null);
    }

    @Override
    public void save(User user) {
        users.add(user);
    }
}
