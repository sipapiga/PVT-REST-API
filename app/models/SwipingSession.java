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

    public void setUserActivityChoice(String userEmailAddress, List<Activity> activities) {

        User user = User.findByEmailAddress(userEmailAddress);
        ActivityChoice choice = new ActivityChoice(user, this, activities);
        choice.save();

        chosenActivities.add(choice);

    }

    public List<Activity> getChosenActivities(String userEmailAddress) {
        return ActivityChoice.findBySwipingSessionAndUser(User.findByEmailAddress(userEmailAddress).id, id).activities;
    }

    public static List<SwipingSession> findByEmail(List<String> emails) {

        Set<User> users = new HashSet<>();
        emails.forEach(email -> users.add(User.findByEmailAddress(email)));

        List<SwipingSession> allSwipingSessions = find.all();

        return allSwipingSessions.stream()
                .filter(swipingSession -> swipingSession.participatingUsers.equals(users)).collect(Collectors.toList());

    }

    public static SwipingSession findById(long id) {
        return find.byId(id);
    }
}
