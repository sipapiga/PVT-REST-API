package models;

import com.avaje.ebean.Model;
import models.accommodation.Accommodation;
import models.user.User;

import javax.persistence.*;

/**
 * @autor Simon Olofsson
 */
@Entity
public class Interest extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne
    public User user;

    @OneToOne
    public Accommodation accommodation;

    public boolean mutual = false;

    public Interest(User user, Accommodation accommodation) {

        this.user = user;
        this.accommodation = accommodation;

    }
}
