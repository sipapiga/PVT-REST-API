package models.accommodation;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Simon Olofsson
 */
@Entity
public class RentalPeriod extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(columnDefinition = "datetime")
    public Date start;

    @Column(columnDefinition = "datetime")
    public Date end;

    public RentalPeriod(Date start, Date end) {

        this.start = start;
        this.end = end;

    }
}
