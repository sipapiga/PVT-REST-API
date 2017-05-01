package models.accommodation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Simon Olofsson
 */
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String streetName;
    public int streetNumber;
    public char streetLetter;

    public String area;

    public double longitude;
    public double latitude;

}
