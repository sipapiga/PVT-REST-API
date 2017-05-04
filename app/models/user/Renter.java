package models.user;

import models.accommodation.Accommodation;
import models.accommodation.Address;

import javax.persistence.*;

/**
 * @author Simon Olofsson
 */
@Entity
@DiscriminatorValue("RENTER")
public class Renter extends User {

    @OneToOne
    public Accommodation accommodation;

    private static Finder<Long, Renter> find = new Finder<>(Renter.class);

    public Renter() {
        super();
    }

    public Renter(String emailAddress, String password, String fullName, String description, int age) {
        super(emailAddress, password, fullName, description, age);
    }

    public Accommodation createAccommodation(double rent, double size, double rooms, double deposit,
                                             boolean smokingAllowed, boolean animalsAllowed,
                                             boolean tv, boolean broadband, String description, Address address) {

        Accommodation accommodation = new Accommodation(rent, size, rooms, deposit,
                smokingAllowed, animalsAllowed, tv, broadband, description, address, this);

        accommodation.save();

        this.accommodation = accommodation;
        save();

        return accommodation;

    }

    public static Renter findByAuthToken(String authToken) {
        return find.where().eq("auth_token", authToken).findUnique();
    }

    public static Renter findByEmailAddress(String emailAddress) {
        return find.where().eq("email_address", emailAddress).findUnique();
    }
}
