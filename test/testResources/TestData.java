/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package testResources;

import models.SwipingSession;
import models.User;
import play.Configuration;
import play.Environment;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestData {

    private User user1;
    private User user2;

    private SwipingSession session;
    private String facebookToken;

    @Inject
    public TestData(Configuration config) {
        
        if (User.findByEmailAddressAndPassword("user1@demo.com", "password") == null) {

            user1 = new User("user1@demo.com", "password", "John Doe");
            user1.save();

            user2 = new User("user2@demo.com", "password", "Jane Doe");
            user2.save();

            session = new SwipingSession("user1@demo.com", "user2@demo.com");
            session.save();

        }

        facebookToken = config.getString("facebookToken");

    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public SwipingSession getSession() {
        return session;
    }

    public String getFacebookToken() {
        return facebookToken;
    }
}
