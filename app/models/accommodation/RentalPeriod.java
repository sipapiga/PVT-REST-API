package models.accommodation;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Simon Olofsson
 */
@Entity
public class RentalPeriod extends Model {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(columnDefinition = "datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date start;

    @Column(columnDefinition = "datetime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date end;

    public RentalPeriod(String start, String end) throws ParseException {

        this.start = dateFormat.parse(start);
        this.end = dateFormat.parse(end);

    }

    public void setStart(String start) throws ParseException{
        this.start = dateFormat.parse(start);

    }

    public void setEnd(String end) throws ParseException {
        this.end = dateFormat.parse(end);
    }
}
