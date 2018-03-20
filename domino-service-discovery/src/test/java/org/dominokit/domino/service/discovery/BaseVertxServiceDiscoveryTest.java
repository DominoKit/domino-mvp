package org.dominokit.domino.service.discovery;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.function.Function;

@RunWith(VertxUnitRunner.class)
public abstract class BaseVertxServiceDiscoveryTest {

    protected static final String SERVICE_NAME = "service name";
    protected static final String SERVICE_HOST = "service host";
    protected static final String ONLY_SPACES = "    ";
    protected static final String EMPTY_STRING = "";
    protected static final String SERVICE_ADDRESS = "service address";
    protected static final String VALUE = "value";
    protected static final String KEY = "key";

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    protected ServiceDiscovery serviceDiscovery;
    protected VertxServiceDiscovery vertxServiceDiscovery;
    protected Vertx vertx;
    protected JsonObject metadata;
    protected JsonObject jsonFilter;

    @Before
    public void setUp() throws Exception {
        vertx = rule.vertx();
        serviceDiscovery = ServiceDiscovery.create(vertx);
        metadata = new JsonObject().put(KEY, VALUE);
        jsonFilter = new JsonObject().put("name", SERVICE_NAME);
        vertxServiceDiscovery = new VertxServiceDiscovery(vertx);
        onSetup();
    }

    @After
    public void tearDown() throws Exception {
        vertx.close();
    }

    protected Function<Record, Boolean> filter() {
        return record -> record.getName().equals(SERVICE_NAME);
    }

    protected abstract void onSetup();
}
