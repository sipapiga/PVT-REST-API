package utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityGenerator {

    private static List<String> generateRandomActivities() {

        List<Activity> existingActivities = Activity.getAll();
        int randomIndex = ThreadLocalRandom.current().nextInt(existingActivities.size());

        List<String> chosenActivities = new ArrayList<>();
        chosenActivities.add(existingActivities.get(randomIndex).name);

        return chosenActivities;

    }

    public static void generateActivitiesAsArrayNode(ObjectNode json) {

        ArrayNode array = json.putArray("activities");
        generateRandomActivities().forEach(activity -> array.add(activity));

    }
}
