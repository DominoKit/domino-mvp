package org.dominokit.domino.service.discovery;

import io.vertx.core.json.JsonObject;
import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;

public abstract class DataSourceServiceDiscoveryTest extends BaseVertxServiceDiscoveryTest {

    private static final String SERVICE_URL = "service url";

    protected DataSourceServiceConfiguration dataSourceConfiguration;
    protected JsonObject dataSourceLocation;

    @Override
    protected void onSetup() {
        dataSourceLocation = new JsonObject().put("url", SERVICE_URL);
        dataSourceConfiguration = new DataSourceServiceConfiguration(SERVICE_NAME, dataSourceLocation);
    }
}
