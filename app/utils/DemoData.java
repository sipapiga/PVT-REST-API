/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.*;
import play.Configuration;
import play.Environment;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DemoData {

    public User user1;
    public User user2;

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

                List<User> participants = new ArrayList<>();

                participants.add(user1);
                participants.add(user2);

                session = new SwipingSession(participants);
                session.save();

                modernaMuseet = new Activity("Moderna Museet");
                modernaMuseet.save();

            }
        }
    }
}
