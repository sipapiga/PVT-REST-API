package testResources;

import models.User;
import org.junit.Before;
import play.test.WithApplication;

public class BaseTest extends WithApplication {

    protected User user1;
    protected String user1Email;
    protected String user1Password;

    protected User user2;
    protected String user2Email;
    protected String user2Password;

    @Before
    public void setupClass() {

        TestData testData = app.injector().instanceOf(TestData.class);

        user1 = testData.getUser1();
        user1Email = user1.getEmailAddress();
        user1Password = user1.getPassword();

        user2 = testData.getUser2();
        user2Email = user2.getEmailAddress();
        user2Password = user2.getPassword();

    }
}
