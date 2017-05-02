package models;

import com.avaje.ebean.Expression;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import models.accommodation.Accommodation;
import models.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;

/**
 * @autor Simon Olofsson
 */
@Entity
public class Interest extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne
    public User user;

    @OneToOne
    @JoinColumn(name = "interest_accommodation_id")
    public Accommodation accommodation;

    public boolean mutual = false;

    private static Finder<Long, Interest> find = new Finder<>(Interest.class);

    public Interest(User user, Accommodation accommodation) {

        this.user = user;
        this.accommodation = accommodation;

    }

    public static List<Interest> filterBy(List<Function<ExpressionList<Interest>, ExpressionList<Interest>>> functions) {

        ExpressionList<Interest> expressionList = find.where();

        for (Function<ExpressionList<Interest>, ExpressionList<Interest>> function : functions) {
            expressionList = function.apply(expressionList);
        }

        return expressionList.findList();

    }
}
