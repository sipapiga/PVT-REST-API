package services.facebook;

import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;

/**
 * @author Simon Olofsson
 */
public interface FacebookCaller {

    CompletionStage<WSResponse> inspectionRequest(String facebookToken, String appToken);
    CompletionStage<WSResponse> dataRequest(String facebookToken, String fields);
}
