package models.accommodation;

import models.user.Renter;

import javax.persistence.*;

/**
 * @author Simon Olofsson
 */
@Entity
public class Accommodation {

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
}
