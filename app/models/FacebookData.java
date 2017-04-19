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
   public String facebookUserId;

   @Column(unique = true, nullable = false)
   @Constraints.MaxLength(255)
   @Constraints.Required
   @Constraints.Email
   public String emailAddress;

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

   public FacebookData() {};

   public FacebookData(String facebookUserId, String emailAddress, String firstName, String lastName, String gender, String locale, int timezone) {

       this.facebookUserId = facebookUserId;
       this.emailAddress = emailAddress;
       this.firstName = firstName;
       this.lastName = lastName;
       this.gender = gender;
       this.locale = locale;
       this.timezone = timezone;

   }

    public static FacebookData findByFacebookUserId(String facebookUserId) {
        return find.where().eq("facebookUserId", facebookUserId).findUnique();
    }

   public static FacebookData findByEmailAddress(String emailAddress) {
      return find.where().eq("emailAddress", emailAddress).findUnique();
   }
}
