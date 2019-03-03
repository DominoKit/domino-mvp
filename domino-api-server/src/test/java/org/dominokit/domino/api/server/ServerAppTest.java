package org.dominokit.domino.api.server;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(VertxUnitRunner.class)
public class ServerAppTest {

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    private ServerApp serverApp;
    private TestRequest request;
    private Vertx vertx;

    @Before
    public void setUp() throws Exception {
        vertx = rule.vertx();
        JsonObject config = new JsonObject();
        config.put("http.port", 0);
        String secretKey = SecretKey.generate();
        RouterConfigurator configurator = new RouterConfigurator(vertx, config, secretKey);
        DominoLauncher.routerHolder.router = configurator.configuredRouter();
        DominoLauncher.configHolder.config = config;
        new DominoLoader(vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config).start();
        serverApp = ServerApp.make();
        request = new TestRequest();


    }

    @Test
    public void testConfigrationWorks() {
        assertTrue(true);
    }

    @After
    public void tearDown() throws Exception {
        vertx.close();
    }
}
