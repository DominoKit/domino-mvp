package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.server.*;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Router;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.createMock;

public abstract class ModuleTestCase {

    protected TestModule testModule;
    protected TestEntryPointContext testEntryPointContext;
    protected Map<String, Object> attributes = new HashMap<>();
    protected Vertx vertx=Vertx.vertx();

    @Before
    public void moduleSetup() {
        testModule = new TestModule();
        ServerConfiguration testServerConfiguration=new TestConfiguration();
        testEntryPointContext = new TestEntryPointContext(createMock(RoutingContext.class), testServerConfiguration);
        testModule.init(testEntryPointContext);
        attributes.clear();
        setUp();
        new ServerConfigurationLoader(new VertxContext(vertx, Router.router(vertx),testServerConfiguration )).loadModules();
        testModule.run();
        onConfigurationCompleted();
    }

    protected abstract void setUp();

    protected void onConfigurationCompleted() {

    }

}