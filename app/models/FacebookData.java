package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * An entity storing data from Facebook.
 *
 * @author Simon Olofsson.
 */
@Entity
public class FacebookData extends Model {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   public long id;

   @Column(unique = true, nullable = false)
   @Constraints.Required
   String facebookUserId;

   @Column(length = 256, unique = true, nullable = false)
   @Constraints.MaxLength(256)
   @Constraints.Required
   @Constraints.Email
   private String emailAddress;

   @Column(length = 256)
   @Constraints.MaxLength(256)
   public String firstName;

   @Column(length = 256)
   @Constraints.MaxLength(256)
   public String lastName;

   @Column(length = 15)
   @Constraints.MaxLength(256)
   public String gender;

   @Column(length = 10)
   @Constraints.MaxLength(10)
   public String locale;

   @Column
   public int timezone;

   @OneToOne
   public User user;

   public static Finder<Long, FacebookData> find = new Finder<>(FacebookData.class);

   public FacebookData(String facebookUserId, String emailAddress, String firstName, String lastName, String gender, String locale, int timezone) {

       this.facebookUserId = facebookUserId;
       this.emailAddress = emailAddress;
       this.firstName = firstName;
       this.lastName = lastName;
       this.gender = gender;
       this.locale = locale;
       this.timezone = timezone;

   }

   public static FacebookData findByEmailAddress(String emailAddress) {
      return find.where().eq("emailAddress", emailAddress).findUnique();
   }
}
