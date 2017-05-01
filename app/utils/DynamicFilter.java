package utils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Simon Olofsson
 */
public class DynamicFilter<T> {

    private List<Predicate<T>> predicates;
    private Predicate<T> compositePredicate;

    private List<T> items;

    public DynamicFilter(List<Predicate<T>> predicates, List<T> items) {

        this.predicates = predicates;
        this.items = items;

    }

    public List<T> filter() {

        // See this excellent SO post: http://stackoverflow.com/questions/22845574/how-to-dynamically-do-filtering-in-java-8
        compositePredicate = predicates.stream().reduce(w ->true, Predicate::and);
        return items.stream().filter(compositePredicate).collect(Collectors.toList());

    }
}
