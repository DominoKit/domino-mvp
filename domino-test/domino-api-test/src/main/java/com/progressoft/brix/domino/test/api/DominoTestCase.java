package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.server.config.ServerConfiguration;
import com.progressoft.brix.domino.api.server.config.ServerConfigurationLoader;
import com.progressoft.brix.domino.api.server.config.VertxConfiguration;
import com.progressoft.brix.domino.api.server.entrypoint.VertxContext;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.service.discovery.VertxServiceDiscovery;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.createMock;

public abstract class DominoTestCase {

    protected TestModule testModule;
    protected VertxEntryPointContext testEntryPointContext;
    protected Map<String, Object> attributes = new HashMap<>();
    protected Vertx vertx = Vertx.vertx();

    @Before
    public void moduleSetup() {
        testModule = new TestModule();
        ServerConfiguration testServerConfiguration = new VertxConfiguration(new JsonObject());
        testEntryPointContext = new VertxEntryPointContext(createMock(RoutingContext.class), testServerConfiguration,
                vertx);
        testModule.init(testEntryPointContext);
        attributes.clear();
        setUp();
        VertxContext vertxContext = VertxContext.VertxContextBuilder.vertx(vertx)
                .router(Router.router(vertx))
                .serverConfiguration(testServerConfiguration)
                .vertxServiceDiscovery(new VertxServiceDiscovery(vertx)).build();
        new ServerConfigurationLoader(vertxContext).loadModules();
        testModule.run();
        onConfigurationCompleted();
    }

    protected abstract void setUp();

    protected void onConfigurationCompleted() {
    }

    public void replacePresenter(String presenterName, TestPresenterFactory presenterFactory) {
        testModule.replacePresenter(presenterName, presenterFactory);
    }

    public void replaceView(String presenterName, TestViewFactory viewFactory) {
        testModule.replaceView(presenterName, viewFactory);
    }

    public <T extends View> T viewOf(String presenterName) {
        return testModule.getView(presenterName);
    }

    public void configureModule(ModuleConfiguration configuration) {
        testModule.configureModule(configuration);
    }

    public TestDominoHistory history() {
        return testModule.history();
    }

    public <C extends Contribution> C contributionOf(Class<C> contributionClass) {
        return testModule.getContribution(contributionClass);
    }

    public void setRoutingListener(TestServerRouter.RoutingListener routingListener) {
        testModule.setRoutingListener(routingListener);
    }

    public void removeRoutingListener() {
        testModule.removeRoutingListener();
    }

    public TestModule.TestResponse forRequest(Class<? extends ClientServerRequest> request) {
        return testModule.forRequest(request);
    }
}