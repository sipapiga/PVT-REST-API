package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.accommodation.Accommodation;
import play.mvc.Result;
import scala.Option;
import utils.DynamicFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static play.mvc.Results.ok;

/**
 * @author Simon Olofsson
 */
public class AccommodationController {

    private Predicate<Double> lessThanOrEqualTo(final Option<Double> option) {
        return  value -> !option.isDefined() || value <= option.get();
    }

    private Predicate<Double> greaterThanOrEqualTo(final Option<Double> option) {
        return value -> !option.isDefined() || value >= option.get();
    }

    private Predicate<Boolean> equalTo(final Option<Boolean> option) {
        return value -> !option.isDefined() || value == option.get();
    }

    public Result get(final Option<Integer> count, final Option<Integer> offset,
                      final Option<Double> rent, final Option<Double> size,
                      final Option<Boolean> smokingAllowed, final Option<Boolean> animalsAllowed) {

        DynamicFilter<Accommodation> accommodationFilter = new DynamicFilter<>(Arrays.asList(

            acc -> !rent.isDefined() || acc.rent <= rent.get(),
            acc -> !size.isDefined() || acc.size >= size.get(),
            acc -> !smokingAllowed.isDefined() || (!smokingAllowed.get() || acc.smokingAllowed), // If the query set smokingAllowed to true, the accommodation has to allow smoking.
            acc -> !animalsAllowed.isDefined() || (!animalsAllowed.get() || acc.animalsAllowed) // If the query set animalsAllowed to true, the accommodation has to allow animals.

        ), Accommodation.findAll());

        List<Accommodation> filteredAccommodation = accommodationFilter.filter();

        ArrayNode arrayNode = new ObjectMapper().valueToTree(filteredAccommodation);

        return ok(arrayNode);

    }
}
