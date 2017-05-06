package services.facebook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.FacebookData;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import repositories.facebookData.FacebookDataRepository;
import repositories.facebookData.FacebookDataStorage;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author Simon Olofsson
 */
public class FacebookService {

    private WSClient ws;
    private FacebookDataStorage facebookDataRepository;

    private static final String FIELDS = "email,first_name,last_name,gender,locale,name,timezone";

    @Inject
    public FacebookService(WSClient ws, FacebookDataStorage facebookDataRepository) {

        this.ws = ws;
        this.facebookDataRepository = facebookDataRepository;

    }

    public CompletionStage<WSResponse> inspectionRequest(String facebookToken, String appId, String appToken, String appName) {

        WSRequest inspectionRequest = ws.url("https://graph.facebook.com/debug_token")
                .setQueryParameter("input_token", facebookToken)
                .setQueryParameter("access_token", appToken);

        return inspectionRequest.get().thenCompose(inspectionData -> {

            if (inspectionData.asJson().findValue("error") != null) {
                return CompletableFuture.completedFuture(inspectionData);
            }

            JsonNode inspectionJson = inspectionData.asJson();

            String retrievedAppId = inspectionJson.findValue("app_id").asText();
            String retrievedAppName = inspectionJson.findValue("application").asText();
            boolean valid = inspectionJson.findValue("is_valid").asBoolean();

            if (!retrievedAppId.equals(appId) || !retrievedAppName.equals(appName) || !valid) {
                return CompletableFuture.completedFuture(inspectionData);
            }

            return dataRequest(facebookToken, FIELDS);

        });
    }

    private CompletionStage<WSResponse> dataRequest(String facebookToken, String fields) {

        WSRequest dataRequest = ws.url("https://graph.facebook.com/me")
                .setQueryParameter("access_token", facebookToken)
                .setQueryParameter("fields", fields);

        return dataRequest.get().thenApply(userData -> {

            try {

                buildFaceBookData(userData.asJson());

            } catch (JsonProcessingException e) {
                return null;
            }

            return userData;

        });
    }

    private void buildFaceBookData(JsonNode data) throws JsonProcessingException {

        String id = data.findValue("id").textValue();

        FacebookData fbData = facebookDataRepository.findByFacebookUserId(id);

        if (fbData == null) {
            facebookDataRepository.create(data);
        } else {
            facebookDataRepository.update(fbData, data);
        }
    }
}
