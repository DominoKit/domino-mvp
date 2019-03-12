package org.dominokit.domino.test.api.client;

import io.vertx.core.json.JsonObject;

public interface CanStartClient {
    void start();
    void start(String configFileName);
    void start(JsonObject additionalConfig);
    void start(String configFileName, JsonObject additionalConfig);
}
