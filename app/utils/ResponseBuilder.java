package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.Result;

import static play.mvc.Results.badRequest;

/**
 * @author Simon Olofsson
 */
public class ResponseBuilder {

    public static final String MALFORMED_REQUEST_BODY = "Malformed request body.";
    public static final String MALFORMED_URI_PARAMETERS = "Malformed URI parameters.";
    public static final String MALFORMED_LIST = "Malformed list.";
    public static final String NO_SUCH_ENTITY = "No such entity.";
    public static final String FORBIDDEN_ACTIVITY_CHOICE = "Forbidden activity choice.";

    private static ObjectMapper mapper = new ObjectMapper();

    public static Result buildBadRequest(String message, String errorType) {

        ObjectNode responseBody = mapper.createObjectNode();

        ObjectNode error = responseBody.putObject("error");

        error.put("type", errorType);
        error.put("message", message);

        return badRequest(responseBody);

    }

    private String getMalformedListMessage(String listContent) {
        return "The list of " + listContent.toLowerCase() + " is malformed, make sure it uses correct json array syntax.";
    }

}
