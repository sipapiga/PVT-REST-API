/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package utils;

import models.*;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.Authorization;
import models.user.Renter;
import models.user.Tenant;
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

    private Renter renter1;
    private Accommodation renter1Accommodation;

    private Tenant tenant1;
    private Interest interest1;

    public SwipingSession session;
    public Activity modernaMuseet;

    @Inject
    public DemoData(Environment environment) {
        
        if (environment.isDev()) {

            if (User.findByEmailAddressAndPassword("user1@demo.com", "password") == null) {

                Logger.info("Loading Demo Data");

                user1 = new User("user1@demo.com", "password", "John Doe");
                user1.createToken();
                user1.save();

                user2 = new User("user2@demo.com", "password", "Jane Doe");
                user2.createToken();
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

                addInterestSpecificData();

            }
        }
    }

    private void addInterestSpecificData() {

        renter1 = new Renter("anna@example.com", "password", "Anna Svensson", "Hej! Jag är en skön prick.", 35);
        renter1.createToken();
        renter1.save();

        Address address = new Address("Dymlingsgränd", 3, 'A', "Hägerstensåsen", 50, 50);
        address.save();

        renter1Accommodation = renter1.createAccommodation(5000, 20, 1, 8000, false, false, true, true, "Schysst ställe!", address);

        tenant1 = new Tenant("kalle@example.com", "password", "Kalle Blomkvist",
                "Hej! Jag letar boende", 23, 1, 5000, 18000, "Karaktär i berättelse", 8000);
        tenant1.createToken();
        tenant1.save();

        interest1 = tenant1.addInterest(renter1Accommodation);

    }
}
