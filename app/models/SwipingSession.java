package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Index;
import com.avaje.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.*;

@Entity
public class SwipingSession extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(nullable = false)
    @Constraints.Required
    @ManyToOne
    public User initiator;

    @ManyToMany
    @JoinTable(name = "initiator_activities")
    public List<Activity> initiatorActivities = new ArrayList<>();

    @Column(nullable = false)
    @Constraints.Required
    @ManyToOne
    public User buddy;

    @ManyToMany
    @JoinTable(name = "buddy_activities")
    public List<Activity> buddyActivities = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    public Date initializationDate;                          // SQL that the DSV mysql server cannot handle.

    private static Finder<Long, SwipingSession> find = new Finder<>(SwipingSession.class);

    public SwipingSession(String initiatorEmail, String buddyEmail) {
        
        this.initiator = User.findByEmailAddress(initiatorEmail);
        this.buddy = User.findByEmailAddress(buddyEmail);
        this.initializationDate = new Date();
        
    }

    public static List<SwipingSession> findByEmail(String initiatorEmail, String buddyEmail) {

        User initiator = User.findByEmailAddress(initiatorEmail);
        User buddy = User.findByEmailAddress(buddyEmail);

        return find.where().eq("initiator_id", initiator.id).eq("buddy_id", buddy.id).findList();

    }

    public static SwipingSession findById(long id) {
        return find.byId(id);
    }
}
