package models;

import com.avaje.ebean.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class SwipingSession extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    /*@Column(nullable = false)
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
    public List<Activity> buddyActivities = new ArrayList<>();*/

    @ManyToMany
    public List<User> participatingUsers = new ArrayList<>();

    /*@ManyToMany
    public Map<User, ActivityChoice> activities = new HashMap<>();*/

    @OneToMany
    public List<ActivityChoice> chosenActivities;

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    public Date initializationDate;                          // SQL that the DSV mysql server cannot handle.

    private static Finder<Long, SwipingSession> find = new Finder<>(SwipingSession.class);

    /*public SwipingSession(String initiatorEmail, String buddyEmail) {

        this.initiator = User.findByEmailAddress(initiatorEmail);
        this.buddy = User.findByEmailAddress(buddyEmail);

        this.participatingUsers.add(User.findByEmailAddress(initiatorEmail));
        this.participatingUsers.add(User.findByEmailAddress(buddyEmail));
        this.initializationDate = new Date();
        
    }*/

    public SwipingSession(List<User> participatingUsers) {

        this.participatingUsers = participatingUsers;
        this.initializationDate = new Date();

    }

    public void setUserActivityChoice(String userEmailAddress, List<Activity> activities) {

        User user = User.findByEmailAddress(userEmailAddress);
        ActivityChoice choice = new ActivityChoice(user, this, activities);
        choice.save();

        chosenActivities.add(choice);
        //activities.put(User.findByEmailAddress(userEmailAddress), choice);

    }

    public List<Activity> getChosenActivities(String userEmailAddress) {
        //return activities.get(User.findByEmailAddress(userEmailAddress)).activities;
        return ActivityChoice.findBySwipingSessionAndUser(User.findByEmailAddress(userEmailAddress).id, id).activities;
    }

    //public static List<SwipingSession> findByEmail(String initiatorEmail, String buddyEmail) {
    public static List<SwipingSession> findByEmail(List<String> emails) {

        List<User> users = new ArrayList<>();
        emails.forEach(email -> users.add(User.findByEmailAddress(email)));

        List<SwipingSession> allSwipingSessions = find.all();

        return allSwipingSessions.stream()
                .filter(swipingSession -> swipingSession.participatingUsers.equals(users)).collect(Collectors.toList());

        /*User initiator = User.findByEmailAddress(initiatorEmail);
        User buddy = User.findByEmailAddress(buddyEmail);*/

        //return find.where().eq("initiator_id", initiator.id).eq("buddy_id", buddy.id).findList();

    }

    public static SwipingSession findById(long id) {
        return find.byId(id);
    }
}
