package utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating activities to pass to users.
 *
 * @author Simon Olofsson
 */
public class ActivityGenerator {

    private static List<String> generateRandomActivities() {

        List<Activity> existingActivities = Activity.getAll();
        int randomIndex = ThreadLocalRandom.current().nextInt(existingActivities.size());

        List<String> chosenActivities = new ArrayList<>();
        chosenActivities.add(existingActivities.get(randomIndex).name);

        return chosenActivities;

    }

    /**
     * Method to generate activities as a Jackson ArrayNode. Takes an
     * ObjectNode, creates an ArrayNode on it and puts the generated
     * activities in it. As such, it modifies the ObjectNode passed
     * and returns nothing.
     *
     * @param json a Jackson ObjectNode to put the ArrayNode in.
     */
    public static void generateActivitiesAsArrayNode(ObjectNode json) {

        ArrayNode array = json.putArray("activities");
        generateRandomActivities().forEach(activity -> array.add(activity));

    }
}
