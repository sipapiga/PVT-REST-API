package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ActivityGenerator {

    public static void generateActivitiesAsArrayNode(ObjectNode json) {

        ArrayNode array = json.putArray("activities");
        array.add("Moderna Museet");

    }
}
