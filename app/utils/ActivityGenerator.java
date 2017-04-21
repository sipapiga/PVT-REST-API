package utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Activity;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityGenerator {

    private static String generateActivity() {

        List<Activity> activities = Activity.getAll();
        int randomIndex = ThreadLocalRandom.current().nextInt(activities.size());

        return activities.get(randomIndex).name;

    }

    public static void generateActivitiesAsArrayNode(ObjectNode json) {

        ArrayNode array = json.putArray("activities");
        array.add(generateActivity());

    }
}
