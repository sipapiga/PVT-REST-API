package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Index;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class SwipingSession extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(nullable = false)
    @Constraints.Required
    @ManyToOne
    public User initiator;

    @Column(nullable = false)
    @Constraints.Required
    @ManyToOne
    public User buddy;

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    public Date initializationDate;                          // SQL that the DSV mysql server cannot handle.

    public static Finder<Long, SwipingSession> find = new Finder<>(SwipingSession.class);

    public SwipingSession(String initiatorEmail, String buddyEmail) {
        
        this.initiator = User.findByEmailAddress(initiatorEmail);
        this.buddy = User.findByEmailAddress(buddyEmail);
        this.initializationDate = new Date();
        
    }

    public static List<SwipingSession> find(String initiatorEmail, String buddyEmail) {

        User initiator = User.findByEmailAddress(initiatorEmail);
        User buddy = User.findByEmailAddress(buddyEmail);

        return find.where().eq("initiator_id", initiator.id).eq("buddy_id", buddy.id).findList();

    }
}
