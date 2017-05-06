package repositories.facebookData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.FacebookData;

/**
 * @author Simon Olofsson
 */
public class FacebookDataRepository implements FacebookDataStorage {

    @Override
    public FacebookData findByFacebookUserId(String facebookUserId) {
        return FacebookData.findByFacebookUserId(facebookUserId);
    }

    @Override
    public FacebookData create(JsonNode data) throws JsonProcessingException {

        FacebookData fbData = new FacebookData();
        update(fbData, data);

        return fbData;

    }

    @Override
    public FacebookData update(FacebookData fbData, JsonNode data) {

        fbData.facebookUserId = data.findValue("id").textValue();
        fbData.emailAddress = data.findValue("email").textValue();
        fbData.firstName = data.findValue("first_name").textValue();
        fbData.lastName = data.findValue("last_name").textValue();
        fbData.gender = data.findValue("gender").textValue();
        fbData.locale = data.findValue("locale").textValue();
        fbData.timezone = data.findValue("timezone").intValue();

        return fbData;

    }

    @Override
    public void save(FacebookData fbData) {
        fbData.save();
    }
}
