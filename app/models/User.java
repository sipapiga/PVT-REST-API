package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * An entity representing a user.
 * Modified from https://github.com/jamesward/play-rest-security
 */
@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    private String authToken;

    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
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

    @Column(nullable = false)
    public Date creationDate;

    @OneToOne
    public FacebookData facebookData;

    public static Finder<Long, User> find = new Finder<>(User.class);

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
