/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package utils;

import models.*;
import models.user.Authorization;
import models.user.User;
import play.Environment;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class DemoData {

    public User user1;
    public User user2;
    public User admin;

    public SwipingSession session;
    public Activity modernaMuseet;

    @Inject
    public DemoData(Environment environment) {
        
        if (environment.isDev()) {

            if (User.findByEmailAddressAndPassword("user1@demo.com", "password") == null) {

                Logger.info("Loading Demo Data");

                user1 = new User("user1@demo.com", "password", "John Doe");
                user1.save();

                user2 = new User("user2@demo.com", "password", "Jane Doe");
                user2.save();

                admin = new User("admin@demo.com", "password", "Sven Svensson");
                admin.authorization = Authorization.ADMIN;
                admin.save();

                Logger.debug(User.findByEmailAddress("admin@demo.com").fullName);

                Set<User> participants = new HashSet<>();

                participants.add(user1);
                participants.add(user2);

                modernaMuseet = new Activity("Moderna Museet");
                modernaMuseet.save();

                Set<Activity> activities = new HashSet<>();
                activities.add(modernaMuseet);

                session = new SwipingSession(participants, activities);
                session.save();


            }
        }
    }
}
