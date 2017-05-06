package testResources.mocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.FacebookData;
import repositories.facebookData.FacebookDataStorage;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Simon Olofsson
 */
public class MockFacebookDataRepository implements FacebookDataStorage {

    private Set<FacebookData> facebookData = new HashSet<>();

    @Override
    public FacebookData findByFacebookUserId(String facebookUserId) {
        return facebookData.stream().filter(facebookData -> facebookData.facebookUserId.equals(facebookUserId)).findFirst().orElse(null);
    }

    @Override
    public FacebookData create(JsonNode data) throws JsonProcessingException {
        return null;
    }

    @Override
    public FacebookData update(FacebookData fbData, JsonNode data) {
        return null;
    }

    @Override
    public void save(FacebookData fbData) {
        facebookData.add(fbData);
    }
}
