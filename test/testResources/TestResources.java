package testResources;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.DatabasePlatform;
import com.google.common.collect.ImmutableMap;
import models.SwipingSession;
import models.User;
import org.junit.rules.ExternalResource;
import play.Logger;
import play.db.Database;
import play.db.Databases;
import play.db.evolutions.Evolutions;

import java.sql.Connection;

public class TestResources extends ExternalResource {

    private Database db;

    private String user1Email = "user1@demo.com";
    private String user1Password = "password";

    private String user2Email = "user2@demo.com";
    private String user2Password = "password";

    private SwipingSession session;

    protected void before() {

        System.setProperty("-Ddb.default.driver", "org.h2.Driver");
        System.setProperty("-Ddb.default.url", "jdbc:h2:mem:play;MODE=MYSQL");

        User user1 = new User("user1@demo.com", "password", "John Doe");
        user1.save();

        User user2 = new User("user2@demo.com", "password", "Jane Doe");
        user2.save();

        SwipingSession session = new SwipingSession("user1@demo.com", "user2@demo.com");
        session.save();

        /*db = Databases.inMemory(ImmutableMap.of(
                "MODE", "MYSQL"
        ));
        Evolutions.applyEvolutions(db);

        Connection connection = db.getConnection();

        String insertUser1 = "INSERT INTO user VALUES ('" + user1Email + "', '" + user1Password + "')";
        connection.prepareStatement()*/

    }

    protected void after() {

        /*Evolutions.cleanupEvolutions(db);
        db.shutdown();*/

    }

    public Database getDB() {
        return db;
    }

    public String getUser1Email() {
        return user1Email;
    }

    public String getUser2Email() {
        return user2Email;
    }

    public SwipingSession getSession() {
        return session;
    }
}
