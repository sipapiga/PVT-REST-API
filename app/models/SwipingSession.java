package models;

import com.avaje.ebean.*;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An entity representing a swiping session between a set of users. Apart from
 * having a set of users associated with it, each swiping session also keeps
 * track of their chosen activities.
 *
 * @author Simon Olofsson
 */
@Entity
public class SwipingSession extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToMany
    public Set<User> participatingUsers = new HashSet<>();

    @OneToMany
    public List<ActivityChoice> chosenActivities;

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    public Date initializationDate;                          // SQL that the DSV mysql server cannot handle.

    private static Finder<Long, SwipingSession> find = new Finder<>(SwipingSession.class);

    public SwipingSession(Set<User> participatingUsers) {

        this.participatingUsers = participatingUsers;
        this.initializationDate = new Date();

    }

    /**
     * Method to record a user's choice of activities.
     *
     * @param userEmailAddress the email address of the user whose choice is to
     *                         be recorded.
     * @param activities the activities to associate with the user.
     */
    public void setUserActivityChoice(String userEmailAddress, List<Activity> activities) {

        User user = User.findByEmailAddress(userEmailAddress);
        ActivityChoice choice = new ActivityChoice(user, this, activities);
        choice.save();

        chosenActivities.add(choice);

    }

    /**
     * Method to get the activities chosen by a user during this swiping session.
     *
     * @param userEmailAddress the email address of the user whose activities
     *                         are to be returned.
     * @return a list containing the activities chosen by the user,
     * may be empty.
     */
    public List<Activity> getChosenActivities(String userEmailAddress) {
        return ActivityChoice.findBySwipingSessionAndUser(User.findByEmailAddress(userEmailAddress).id, id).activities;
    }

    /**
     * Method to get all swiping sessions where all of the users and exactly
     * those users associate with the passed email addresses have taken part.
     * That is, will only return those swiping sessions whose set of users
     * are exactly equal to those indicated by the email addresses.
     *
     * @param emails a list of emails to match swiping sessions against.
     * @return a list of swiping sessions that are associated with exactly
     * those users indicated by the email addresses passed.
     */
    public static List<SwipingSession> findByEmail(List<String> emails) {

        Set<User> users = new HashSet<>();
        emails.forEach(email -> users.add(User.findByEmailAddress(email)));

        List<SwipingSession> allSwipingSessions = find.all();

        return allSwipingSessions.stream()
                .filter(swipingSession -> swipingSession.participatingUsers.equals(users)).collect(Collectors.toList());

    }

    /**
     * Method to find a swiping session by its id.
     *
     * @param id the id of the swiping session to get.
     * @return the swiping session indicated by the id, null if there is none.
     */
    public static SwipingSession findById(long id) {
        return find.byId(id);
    }
}
