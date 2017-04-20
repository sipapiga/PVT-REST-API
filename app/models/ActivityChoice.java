package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class ActivityChoice extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Activity> chosenActivities;

    public ActivityChoice(List<Activity> chosenActivities) {
        this.chosenActivities = chosenActivities;
    }
}
