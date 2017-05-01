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

    public Renter() {
        super();
    }
}
