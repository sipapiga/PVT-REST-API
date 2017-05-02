package models.user;

import models.accommodation.Accommodation;

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

    public static Renter findByAuthToken(String authToken) {
        return find.where().eq("auth_token", authToken).findUnique();
    }

    public static Renter findByEmailAddress(String emailAddress) {
        return find.where().eq("email_address", emailAddress).findUnique();
    }
}
