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

    public Tenant() {
        super();
    }
}
