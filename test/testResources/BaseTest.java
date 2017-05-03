package testResources;

import models.Interest;
import models.accommodation.Accommodation;
import models.user.Renter;
import models.user.Tenant;
import models.user.User;
import org.junit.Before;
import play.test.WithApplication;

public class BaseTest extends WithApplication {

    protected User user1;
    protected String user1Email;
    protected String user1Password;

    protected User user2;
    protected String user2Email;
    protected String user2Password;

    protected User admin;
    protected String adminEmail;
    protected String adminPassword;

    protected Renter renter1;
    protected String renter1Email;
    protected String renter1Password;

    protected Tenant tenant1;
    protected String tenant1Email;
    protected String tenant1Password;

    protected Interest interest1;

    protected Accommodation renter1Accommodation;

    protected String facebookToken;

    @Before
    public void setupClass() {

        TestData testData = app.injector().instanceOf(TestData.class);

        user1 = testData.getUser1();
        user1Email = user1.getEmailAddress();
        user1Password = user1.getPassword();

        user2 = testData.getUser2();
        user2Email = user2.getEmailAddress();
        user2Password = user2.getPassword();

        admin = testData.getAdmin();
        adminEmail = admin.getEmailAddress();
        adminPassword = admin.getPassword();

        renter1 = testData.getRenter1();
        renter1Email = renter1.getEmailAddress();
        renter1Password = renter1.getPassword();

        renter1Accommodation = testData.getRenter1Accommodation();

        tenant1 = testData.getTenant1();
        tenant1Email = tenant1.getEmailAddress();
        tenant1Password = tenant1.getPassword();

        interest1 = testData.getInterest1();

        facebookToken = testData.getFacebookToken();

    }
}
