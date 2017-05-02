package models.user;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.Interest;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * An entity representing a user.
 * Modified from https://github.com/jamesward/play-rest-security
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("USER")
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    private String authToken;

    @Column(unique = true, nullable = false)
    @Constraints.MaxLength(255)
    @Constraints.Required
    @Constraints.Email
    private String emailAddress;

    @Column(length = 64)
    private byte[] shaPassword;

    @Transient
    @Constraints.MinLength(6)
    @Constraints.MaxLength(256)
    @JsonIgnore
    private String password;

    @Column()
    private byte[] shaFacebookAuthToken;

    @Transient
    @JsonIgnore
    private String facebookAuthToken;

    @Column(length = 256, nullable = false)
    @Constraints.Required
    @Constraints.MinLength(2)
    @Constraints.MaxLength(256)
    public String fullName;

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    public Date creationDate;                                // SQL that the DSV mysql server cannot handle.

    public String description;

    public int age;

    @OneToOne
    public FacebookData facebookData;

    @ManyToOne
    public Authorization authorization = Authorization.USER;

    private static Finder<Long, User> find = new Finder<>(User.class);

    public User() {
        this.creationDate = new Date();
    }

    public User(String emailAddress, String password, String fullName) {

        setEmailAddress(emailAddress);
        setPassword(password);
        this.fullName = fullName;
        this.creationDate = new Date();

    }

    public User(String emailAddress, String fullName) {

        setEmailAddress(emailAddress);
        this.fullName = fullName;
        this.creationDate = new Date();

    }

    public User(String emailAddress, String password, String fullName, String description, int age) {

        setEmailAddress(emailAddress);
        setPassword(password);

        this.fullName = fullName;
        this.creationDate = new Date();
        this.description = description;
        this.age = age;

    }

    public static byte[] getSha512(String value) {
        
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
        shaPassword = getSha512(password);

    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFacebookAuthToken(String facebookAuthToken) {

        this.facebookAuthToken = facebookAuthToken;
        shaFacebookAuthToken = getSha512(facebookAuthToken);

    }

    public void setFacebookData(FacebookData facebookData) {
        this.facebookData = facebookData;
    }

    public String createToken() {

        authToken = UUID.randomUUID().toString();
        save();

        return authToken;

    }

    public void deleteAuthToken() {

        authToken = null;
        save();

    }

    public static User findByAuthToken(String authToken) {
        
        if (authToken == null) {
            return null;
        }

        try {
            return find.where().eq("authToken", authToken).findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public static User findByEmailAddress(String emailAddress) {
        return find.where().eq("emailAddress", emailAddress).findUnique();
    }

    public static User findByEmailAddressAndPassword(String emailAddress, String password) {
        return find.where().eq("emailAddress", emailAddress.toLowerCase()).eq("shaPassword", getSha512(password)).findUnique();
    }
}
