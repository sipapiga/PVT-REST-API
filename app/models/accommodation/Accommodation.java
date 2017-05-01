package models.accommodation;

import com.avaje.ebean.Model;
import models.user.Renter;

import javax.persistence.*;
import java.util.List;

/**
 * @author Simon Olofsson
 */
@Entity
public class Accommodation extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public double rent;
    public double size;
    public double rooms;
    public double deposit;
    public boolean smokingAllowed;
    public boolean animalsAllowed;
    public boolean tv;
    public boolean broadband;
    public String description;

    @OneToOne
    public Renter renter;

    @ManyToOne
    public Address address;

    private static Finder<Long, Accommodation> find = new Finder<>(Accommodation.class);

    public Accommodation(double rent, double size, double rooms, double deposit,
                         boolean smokingAllowed, boolean animalsAllowed, boolean tv, boolean broadband,
                         String description, Renter renter) {

        this.rent = rent;
        this.size = size;
        this.rooms = rooms;
        this.deposit = deposit;
        this.smokingAllowed = smokingAllowed;
        this.animalsAllowed = animalsAllowed;
        this.tv = tv;
        this.broadband = broadband;
        this.description = description;
        this.renter = renter;

    }

    public static List<Accommodation> findAll() {
        return find.all();
    }
}
