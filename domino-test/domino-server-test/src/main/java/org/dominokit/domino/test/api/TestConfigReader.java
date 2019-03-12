package org.dominokit.domino.test.api;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class TestConfigReader {

    private JsonObject testConfig;

    public TestConfigReader(Vertx vertx, String fileName) {
        testConfig = vertx.fileSystem().readFileBlocking(fileName).toJsonObject();
    }

    public JsonObject getTestConfig() {
        return testConfig;
    }

    public String getConfigValue(String configKey) {
        return testConfig.getString(configKey);
    }
}
