package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Index;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import javax.persistence.*;
import java.util.List;

@Entity
@Index(columnNames = {"user_id", "swiping_session_id"})
public class ActivityChoice extends Model {

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
    public List<Activity> activities;

    private static Finder<Long, ActivityChoice> find = new Finder<>(ActivityChoice.class);

    public ActivityChoice(User user, SwipingSession swipingSession, List<Activity> activities) {

        this.user = user;
        this.swipingSession = swipingSession;
        this.activities = activities;

    }

    public static ActivityChoice findBySwipingSessionAndUser(long userId, long swipingSessionId) {
        return find.where().eq("user_id", userId).eq("swiping_session_id", swipingSessionId).findUnique();
    }
}
