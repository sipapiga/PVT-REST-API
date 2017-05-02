package models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.Interest;
import models.accommodation.Accommodation;

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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    public List<Interest> interests;

    private static Finder<Long, Tenant> find = new Finder<>(Tenant.class);

    public Tenant() {
        super();
    }

    public Tenant(String emailAddress, String password, String fullName,
                  String description, int age, int numberOfTenants,
                  int maxRent, double income, String occupation, double deposit) {

        super(emailAddress, password, fullName, description, age);
        this.numberOfTenants = numberOfTenants;
        this.maxRent = maxRent;
        this.income = income;
        this.occupation = occupation;
        this.deposit = deposit;

    }

    public void addInterest(Accommodation accommodation) {

       Interest interest = new Interest(this, accommodation);
       interest.save();

       interests.add(interest);

       save();

    }

    public static Tenant findByAuthToken(String authToken) {
        return find.where().eq("auth_token", authToken).findUnique();
    }

    public static Tenant findByEmailAddress(String emailAddress) {
        return find.where().eq("email_address", emailAddress).findUnique();
    }

}
