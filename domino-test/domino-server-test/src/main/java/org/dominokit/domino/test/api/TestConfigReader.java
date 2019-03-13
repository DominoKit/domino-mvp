package org.dominokit.domino.test.api;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfigReader.class);
    private JsonObject testConfig;

    public TestConfigReader(Vertx vertx, String fileName) {
        try {
            testConfig = vertx.fileSystem().readFileBlocking(fileName).toJsonObject();
        } catch (Exception e) {
            LOGGER.warn("no test config.json file provided", e.getLocalizedMessage());
            testConfig = new JsonObject();
        }
    }

    public JsonObject getTestConfig() {
        return testConfig;
    }

    public String getConfigValue(String configKey) {
        return testConfig.getString(configKey);
    }
}
