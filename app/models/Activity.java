package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

@Entity
public class Activity extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(unique = true, nullable = false)
    @Constraints.Required
    public String name;

    private static Finder<Long, Activity> find = new Finder<>(Activity.class);

    public Activity(String name) {
        this.name = name;
    }

    public static Activity findById(long id) {
        return find.byId(id);
    }

    public static Activity findByName(String name) {
        return find.where().eq("name", name).findUnique();
    }
}
