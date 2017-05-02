package models.user;

import models.Interest;

import javax.persistence.*;
import java.util.List;

/**
 * @author Simon Olofsson
 */
@Entity
@DiscriminatorValue("TENANT")
public class Tenant extends User {

    public int numberOfTenants = 1;
    public int maxRent;
    public double income;
    public String occupation;
    public double deposit;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Interest> interests;

    private static Finder<Long, Tenant> find = new Finder<>(Tenant.class);

    public Tenant() {
        super();
    }

    public static Tenant findByAuthToken(String authToken) {
        return find.where().eq("auth_token", authToken).findUnique();
    }

    public static Tenant findByEmailAddress(String emailAddress) {
        return find.where().eq("email_address", emailAddress).findUnique();
    }
}
