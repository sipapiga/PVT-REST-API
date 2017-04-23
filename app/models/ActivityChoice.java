package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Index;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * A utility entity mapping a swiping session to a user and a series of chosen
 * activities. Swiping sessions and users can have many activity choices,
 * whereas an activity choice potentially includes many activities but is
 * always linked to exactly one user and one swiping session.
 *
 * @author Simon Olofsson
 */
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "swiping_session_id"}))
@Entity
public class ActivityChoice extends Model {

    /*
     * Ideally, this entity would have been represented by a
     * Map<User, List<Activity>> in SwipingSession but this does not seem to
     * be possible with Ebeans. Another thought would have been to keep this
     * entity but reduce it to a wrapper around the List<Activity> and keep a
     * Map<User, ActivityChoice> in SwipingSession, but that doesn't seem to
     * work either.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;

    @ManyToOne
    public User user;

    @ManyToOne
    @JsonIgnore
    public SwipingSession swipingSession;

    @ManyToMany(cascade = CascadeType.ALL)
    public Set<Activity> activities;

    private static Finder<Long, ActivityChoice> find = new Finder<>(ActivityChoice.class);

    public ActivityChoice(User user, SwipingSession swipingSession, Set<Activity> activities) {

        this.user = user;
        this.swipingSession = swipingSession;
        this.activities = activities;

    }

    /**
     * Method to find a choice of activities based on the swiping session
     * during which it took place and the user who made the choice.
     *
     * @param userId the id of the user who made the choice.
     * @param swipingSessionId the id of the swiping session during which the
     *                         choice was made.
     * @return The ActivityChoice with the indicated user id and swiping
     * session id, null if there is none.
     */
    public static ActivityChoice findBySwipingSessionAndUser(long userId, long swipingSessionId) {
        return find.where().eq("user_id", userId).eq("swiping_session_id", swipingSessionId).findUnique();
    }
}
