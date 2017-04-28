package utils;

import com.fasterxml.jackson.databind.JsonNode;
import models.FacebookData;
import models.User;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;

/**
 * @author Simon Olofsson
 */
public class FacebookDataGatherer {

    private String userToken;

    public CompletionStage<WSResponse> gather(WSClient ws, String facebookToken, WSResponse userData) {

        String userId = processUserData(facebookToken, userData);
        return ws.url("https://graph.facebook.com/" + userId + "/friendlists?access_token=" + facebookToken).get();

    }

    /**
     * Takes data from Facebook and determines whether the user already exists
     * or not, and creates or updates the user and its fields accordingly.
     *
     * @param facebookToken a Facebook authorization token.
     * @param userData a WSResponse object containing the user data.
     * @return the user id extracted from the data.
     */
    private String processUserData(String facebookToken, WSResponse userData) {

        JsonNode jsonData = userData.asJson();
        String email = jsonData.findValue("email").textValue();

        User user = User.findByEmailAddress(email);

        if (user == null) {

            user = new User(email, jsonData.findValue("name").textValue());
            user.save();

        }

        setUserAttributes(user, userData.asJson(), facebookToken);

        String userId = userData.asJson().findValue("id").textValue();
        userToken = user.createToken();

        return userId;

    }

    /**
     * Sets user attributes according to the data passed to the method. Only
     * operates on existing users and will create a new FacebookData entry
     * from the data passed.
     *
     * @param user the user to operate on.
     * @param userData the data to create a FacebookData entry from.
     * @param facebookToken the Facebook authorization token used to
     *                      obtain the data. Will be included in the
     *                      FacebookData entry for further use.
     */
    private void setUserAttributes(User user, JsonNode userData, String facebookToken) {

        FacebookData fbData = buildFaceBookData(userData);
        fbData.save();

        user.setFacebookData(fbData);
        user.setFacebookAuthToken(facebookToken);

        user.save();

    }

    private FacebookData buildFaceBookData(JsonNode data) {

        String id = data.findValue("id").textValue();

        FacebookData fbData = FacebookData.findByFacebookUserId(id);
        if (fbData == null) {
            fbData = new FacebookData();
        }

        fbData.facebookUserId = id;
        fbData.emailAddress = data.findValue("email").textValue();
        fbData.firstName = data.findValue("first_name").textValue();
        fbData.lastName = data.findValue("last_name").textValue();
        fbData.gender = data.findValue("gender").textValue();
        fbData.locale = data.findValue("locale").textValue();
        fbData.timezone = data.findValue("timezone").intValue();

        return fbData;

    }
}
