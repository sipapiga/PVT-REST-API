package services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.routing.Router;
import play.routing.RoutingDsl;
import play.server.Server;
import testResources.BaseTest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static play.mvc.Results.ok;

/**
 * @author Simon Olofsson
 */
public class FacebookServiceTest extends BaseTest {

    private Server server;
    private WSClient ws;
    private FacebookService facebookService;

    /*
        {
            "data": {
                "app_id":"",
                "application":"",
                "expires_at":1493838000,
                "is_valid":true,
                "scopes":[
                    "email","public_profile"
                ],
                "user_id":"10154623247991818"
            }
        }
     */

    @Before
    public void setup() {

        String graphApi = "https://graph.facebook.com/";

        ObjectNode responseBody = Json.newObject();
        ObjectNode data = responseBody.putObject("data");

        data.put("app_id", appId);
        data.put("application", appName);
        data.put("expires_at", 1493838000);
        data.put("is_valid", true);

        ArrayNode scopes = data.putArray("scopes");

        scopes.add("email");
        scopes.add("public_profile");

        data.put("user_id", "10154623247991818");

        Router router = new RoutingDsl()
                .GET(graphApi + "/debug_token?input_token=123&access_token=1234").routeTo(() -> {
                    return ok(responseBody);
                }).GET(graphApi + "me?access_token=123&fields=email,first_name,last_name,gender,locale,name,timezone").routeTo(() -> {
                    return ok(responseBody);
                })
                .build();

        server = Server.forRouter(router);
        ws = WS.newClient(server.httpPort());
        facebookService = new FacebookService(ws);

    }

    @After
    public void tearDown() throws IOException {

        try {
            ws.close();
        }
        finally {
            server.stop();
        }
    }

    @Test
    public void canMakeValidRequest() throws Exception {

        /*facebookService.inspectionRequest("123", appId, appToken, appName).thenAccept(userData -> {
            Logger.debug(userData.asJson().toString());
        }).toCompletableFuture().get(10, TimeUnit.SECONDS);*/

    }

}
