package org.dominokit.domino.test.api.client;

import io.vertx.core.json.JsonObject;

public interface CanStartClient {
    void start();
    void start(DominoTestClient.StartCompletedHandler onCompletedHandler);
    void start(String configFileName);
    void start(String configFileName, DominoTestClient.StartCompletedHandler onCompletedHandler);
    void start(JsonObject additionalConfig);
    void start(JsonObject additionalConfig, DominoTestClient.StartCompletedHandler onCompletedHandler);
    void start(String configFileName, JsonObject additionalConfig);
    void start(String configFileName, JsonObject additionalConfig, DominoTestClient.StartCompletedHandler onCompletedHandler);
}
