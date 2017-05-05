/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package testResources;

import models.Activity;
import models.Interest;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.accommodation.RentalPeriod;
import models.user.*;
import models.SwipingSession;
import play.Configuration;
import play.Logger;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class TestData {

    private User user1;
    private User user2;
    private User admin;

    private Renter renter1;
    private Accommodation renter1Accommodation;

    private Tenant tenant1;
    private Interest interest1;

    private SwipingSession session;
    private Activity modernaMuseet;

    private String facebookToken;
    private String appToken;
    private String appName;
    private String appId;

    @Inject
    public TestData(Configuration config) {
        
        if (User.findByEmailAddressAndPassword("user1@demo.com", "password") == null) {

            user1 = new User("user1@demo.com", "password", "John Doe");
            user1.save();

            user2 = new User("user2@demo.com", "password", "Jane Doe");
            user2.save();

            admin = new User("admin@demo.com", "password", "Sven Svensson");
            admin.authorization = Authorization.ADMIN;
            admin.save();

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

        facebookToken = config.getString("facebookToken");
        appToken = config.getString("appToken");
        appName = config.getString("appName");
        appId = config.getString("appId");

    }

    private void addInterestSpecificData() {

        renter1 = new Renter("anna@example.com", "password", "Anna Svensson", "Hej! Jag är en skön prick.", 35);
        renter1.save();

        Address address = new Address("Dymlingsgränd", 3, 'A', "Hägerstensåsen", 50, 50);
        address.save();

        renter1Accommodation = renter1.createAccommodation(5000, 20, 1, 8000, false, false, true, true, "Schysst ställe!", address);

        FacebookData tenantFacebookData = new FacebookData("tenant1fb","kalle@example.com","Kalle", "Blomkvist", "male", "en_Us", 888);
        tenantFacebookData.save();

        tenant1 = new Tenant("kalle@example.com", "password", "Kalle Blomkvist",
                "Hej, Jag heter Kalle och behöver någonstans att bo.", 23, 1, 5000, 18000, "Karaktär i berättelse", 8000);
        try {
            RentalPeriod rentalPeriod = new RentalPeriod("2017-05-01", "2018-05-01");

            rentalPeriod.save();
            tenant1.facebookData = tenantFacebookData;
            tenant1.rentalPeriod = rentalPeriod;



        }catch(ParseException e){
            Logger.debug("Could not create rental period for tenant1");
        }
        tenant1.save();
        Logger.debug("This is the place"+ tenant1.rentalPeriod.start.toString());
        interest1 = tenant1.addInterest(renter1Accommodation);

    }



    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public User getAdmin() {
        return admin;
    }

    public Renter getRenter1() {
        return renter1;
    }

    public Accommodation getRenter1Accommodation() {
        return renter1Accommodation;
    }

    public Tenant getTenant1() {
        return tenant1;
    }

    public Interest getInterest1() {
        return interest1;
    }

    public SwipingSession getSession() {
        return session;
    }

    public Activity getModernaMuseet() {
        return modernaMuseet;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public String getAppToken() {
        return appToken;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppId() {
        return appId;
    }
}
