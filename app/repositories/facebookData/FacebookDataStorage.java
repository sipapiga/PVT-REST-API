package repositories.facebookData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.FacebookData;

/**
 * @author Simon Olofsson
 */
public interface FacebookDataStorage {

    FacebookData findByFacebookUserId(String facebookUserId);

    FacebookData create(JsonNode data) throws JsonProcessingException;

    FacebookData update(FacebookData fbData, JsonNode data);

    void save (FacebookData fbData);

}