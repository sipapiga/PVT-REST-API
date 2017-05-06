package repositories.users;

import com.avaje.ebean.Model;
import models.user.Tenant;
import models.user.User;

/**
 * @author Simon Olofsson
 */
public class UsersRepository implements UserStorage {

    @Override
    public User findByAuthToken(String authToken) {
        return User.findByAuthToken(authToken);
    }

    @Override
    public Tenant findTenantById(long tenantId) {
        return Tenant.findById(tenantId);
    }

    @Override
    public User findByEmailAddress(String email) {
        return User.findByEmailAddress(email);
    }

    @Override
    public User findByEmailAddressAndPassword(String emailAddress, String password) {
        return User.findByEmailAddressAndPassword(emailAddress, password);
    }

    @Override
    public void save(User user) {
        user.save();
    }


}
