package services;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author Simon Olofsson
 */
public class FacebookService {

    private WSClient ws;
    private static final String FIELDS = "email,first_name,last_name,gender,locale,name,timezone";

    @Inject
    public FacebookService(WSClient ws) {
        this.ws = ws;
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

    public CompletionStage<WSResponse> dataRequest(String facebookToken, String fields) {

        WSRequest dataRequest = ws.url("https://graph.facebook.com/me")
                .setQueryParameter("access_token", facebookToken)
                .setQueryParameter("fields", fields);

        return dataRequest.get();

    }

}
