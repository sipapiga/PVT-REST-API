package utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating activities to pass to users.
 *
 * @author Simon Olofsson
 */
public class ActivityGenerator {

    private Set<Activity> generatedActivities;

    public ActivityGenerator() {

        List<Activity> existingActivities = Activity.getAll();
        int randomIndex = ThreadLocalRandom.current().nextInt(existingActivities.size());

        generatedActivities = new HashSet<>();
        generatedActivities.add(existingActivities.get(randomIndex));

    }

    /**
     * Method to get activities as a Jackson ArrayNode. Takes an
     * ObjectNode, creates an ArrayNode on it and puts the generated
     * activities in it. As such, it modifies the ObjectNode passed
     * and returns nothing.
     *
     * @param json a Jackson ObjectNode to put the ArrayNode in.
     */
    public void getActivitiesAsArrayNode(ObjectNode json) {

        ArrayNode array = json.putArray("activities");
        generatedActivities.forEach(activity -> array.add(activity.name));

    }

    public Set<Activity> getActivitiesAsSet() {
        return generatedActivities; // Returning generatedActivities directly breaks encapsulation. Might not be a problem, however.
    }
}
