package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SwipingSession extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
    String initiatorEmail;

    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
    String buddyEmail;

    @Column(nullable = false)
    public Date initializationDate;

    public static Finder<Long, SwipingSession> find = new Finder<>(SwipingSession.class);

    public SwipingSession(String initiatorEmail, String buddyEmail) {
        
        this.initiatorEmail = initiatorEmail;
        this.buddyEmail = buddyEmail;
        this.initializationDate = new Date();
        
    }

    public static SwipingSession find(String initiatorEmail, String buddyEmail) {
        return find.where().eq("initiator_email", initiatorEmail.toLowerCase()).eq("buddy_email", buddyEmail.toLowerCase()).findUnique();
    }
}
