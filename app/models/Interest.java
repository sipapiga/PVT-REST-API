package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.*;
import models.accommodation.Accommodation;
import models.user.Tenant;
import models.user.User;
import play.Logger;
import scala.Option;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
@Entity
public class Interest extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;

    /*
     * The JsonIdentity annotations make sure that only id is serialized.
     */
    @OneToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("tenantId")
    public Tenant tenant;

    @OneToOne
    @JoinColumn(name = "interest_accommodation_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("accommodationId")
    public Accommodation accommodation;

    public boolean mutual = false;

    private static Finder<Long, Interest> find = new Finder<>(Interest.class);

    public Interest(Tenant tenant, Accommodation accommodation) {

        this.tenant = tenant;
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
