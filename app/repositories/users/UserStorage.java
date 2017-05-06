package repositories.users;

import models.user.Tenant;
import models.user.User;

/**
 * @author Simon Olofsson
 */
public interface UserStorage {

    User findByAuthToken(String authToken);

    Tenant findTenantById(long tenantId);

    User findByEmailAddress(String email);

    void save(User user);

    User findByEmailAddressAndPassword(String emailAddress, String password);
}
