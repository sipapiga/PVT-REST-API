package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class SwipingSession extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    String initiatorEmail;

    @Column(unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    String buddyEmail;

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    public Date initializationDate;                          // SQL that the DSV mysql server cannot handle.

    public static Finder<Long, SwipingSession> find = new Finder<>(SwipingSession.class);

    public SwipingSession(String initiatorEmail, String buddyEmail) {
        
        this.initiatorEmail = initiatorEmail;
        this.buddyEmail = buddyEmail;
        this.initializationDate = new Date();
        
    }

    public static List<SwipingSession> find(String initiatorEmail, String buddyEmail) {
        //return find.where().eq("initiator_email", initiatorEmail.toLowerCase()).eq("buddy_email", buddyEmail.toLowerCase()).findUnique();
       return find.where().eq("initiator_email", initiatorEmail.toLowerCase()).eq("buddy_email", buddyEmail.toLowerCase()).findList();
    }
}
