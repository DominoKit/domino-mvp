package org.dominokit.domino.test.api.client;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.ModuleConfigurator;
import org.dominokit.domino.api.client.mvp.Store;
import org.dominokit.domino.api.client.mvp.StoreRegistry;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.api.server.config.ServerConfigurationLoader;
import org.dominokit.domino.api.server.config.VertxConfiguration;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.api.server.entrypoint.VertxEntryPointContext;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.api.shared.request.ServerRequest;
import org.dominokit.domino.service.discovery.VertxServiceDiscovery;
import org.dominokit.domino.test.api.DominoTestServer;
import org.dominokit.domino.test.api.TestConfigReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.dominokit.domino.api.client.ClientApp.make;
import static org.easymock.EasyMock.createMock;

public class DominoTestClient implements CanCustomizeClient, CanStartClient,
        ClientContext {

    public static final Logger LOGGER = Logger.getLogger(DominoTestClient.class.getName());

    private List<ModuleConfiguration> modules;
    private List<ListenerOf> listenersOf = new ArrayList<>();

    private VertxEntryPointContext testEntryPointContext;
    private Vertx vertx;
    private boolean withServer = false;
    private TestContext testContext;

    private BeforeClientStart beforeClientStart = context -> {
    };

    private StartCompleted startCompleted = context -> {
    };

    private DominoTestServer.AfterLoadHandler afterLoadHandler = context -> {
    };

    private ConfigOverrideHandler configOverrideHandler = () -> {
    };

    private DominoTestClient(ModuleConfiguration... configurations) {
        this(Arrays.asList(configurations));
    }

    private DominoTestClient(List<ModuleConfiguration> configurations) {
        this.modules = configurations;
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(1000 * 60 * 60);
        vertxOptions.setWorkerPoolSize(50);
        vertx =Vertx.vertx(vertxOptions);
        init();
    }

    public static CanCustomizeClient useModules(ModuleConfiguration... configurations) {
        return new DominoTestClient(configurations);
    }

    public static CanCustomizeClient useModules(List<ModuleConfiguration> configurations) {
        return new DominoTestClient(configurations);
    }

    @Override
    public CanCustomizeClient overrideConfig(ConfigOverrideHandler handler) {
        this.configOverrideHandler = handler;
        return this;
    }

    @Override
    public <L extends DominoEventListener> CanCustomizeClient listenerOf(Class<L> listenerType,
                                                                         ListenerHandler<L> handler) {
        this.listenersOf.add(new ListenerOf(listenerType, handler));
        return this;
    }

    @Override
    public CanCustomizeClient onBeforeClientStart(BeforeClientStart beforeClientStart) {
        this.beforeClientStart = beforeClientStart;
        return this;
    }

    @Override
    public CanCustomizeClient onClientStarted(StartCompleted startCompleted) {
        this.startCompleted = startCompleted;
        return this;
    }

    @Override
    public CanCustomizeClient withServer(TestContext testContext, DominoTestServer.AfterLoadHandler afterLoadHandler) {
        this.afterLoadHandler = afterLoadHandler;
        return withServer(testContext);
    }

    @Override
    public CanCustomizeClient withServer(TestContext testContext) {
        this.testContext = testContext;
        this.withServer = true;
        return this;
    }

    @Override
    public void start(String configFileName, JsonObject additionalConfig) {
        start(configFileName, additionalConfig, () -> {
        });
    }

    @Override
    public void start(String configFileName, JsonObject additionalConfig, StartCompletedHandler onCompleteHandler) {

        JsonObject config = new TestConfigReader(vertx, configFileName).getTestConfig();
        additionalConfig.mergeIn(config);
        ServerConfiguration testServerConfiguration = new VertxConfiguration(config);
        testEntryPointContext = new VertxEntryPointContext(createMock(RoutingContext.class), testServerConfiguration,
                vertx);
        VertxContext vertxContext = VertxContext.VertxContextBuilder.vertx(vertx)
                .router(Router.router(vertx))
                .serverConfiguration(testServerConfiguration)
                .vertxServiceDiscovery(new VertxServiceDiscovery(vertx))
                .configRetriever(ConfigRetriever.create(vertx))
                .build();
        new ServerConfigurationLoader(vertxContext).loadModules();

        modules.forEach(this::configureModule);

        this.configOverrideHandler.overrideConfig();
        SlotRegistry.registerSlot(ViewBaseClientPresenter.DOCUMENT_BODY, new FakeSlot());

        listenersOf.forEach(c -> c.handler.handle(getListener(c.listenerName)));

        if (withServer) {
            Async async = testContext.async();
            LOGGER.info("Starting server ... ");
            DominoTestServer.vertx(vertx())
                    .onAfterLoad(serverContext -> {
                        getDominoOptions().setPort(serverContext.getActualPort());
                        afterLoadHandler.handle(serverContext);
                        doStart(() -> {
                            startCompleted.onStarted(this);
                            onCompleteHandler.onStarted();
                            LOGGER.info("Server started on port [" + serverContext.getActualPort() + "]");
                            async.complete();
                        });
                    })
                    .start();
        } else {
            doStart(() -> {
                startCompleted.onStarted(this);
                onCompleteHandler.onStarted();
            });
        }
    }

    @Override
    public void start() {
        start("config.json", () -> {
        });
    }

    @Override
    public void start(DominoTestClient.StartCompletedHandler onCompletedHandler) {
        start("config.json", onCompletedHandler);
    }

    @Override
    public void start(String configFileName) {
        start(configFileName, new JsonObject(), () -> {
        });
    }

    @Override
    public void start(String configFileName, DominoTestClient.StartCompletedHandler onCompletedHandler) {
        start(configFileName, new JsonObject(), onCompletedHandler);
    }

    @Override
    public void start(JsonObject additionalConfig) {
        start("config.json", additionalConfig, () -> {
        });
    }

    @Override
    public void start(JsonObject additionalConfig, DominoTestClient.StartCompletedHandler onCompletedHandler) {
        start("config.json", additionalConfig, onCompletedHandler);
    }

    private void doStart(ApplicationStartHandler applicationStartHandler) {
        getDominoOptions().setApplicationStartHandler(applicationStartHandler);
        make().run(dominoOptions -> beforeClientStart.onBeforeStart(DominoTestClient.this));
        LOGGER.info("Test client started.");
    }

    private void init() {
        TestClientAppFactory.make(vertx);
    }

    private void configureModule(ModuleConfiguration configuration) {
        new ModuleConfigurator().configureModule(configuration);
    }

    public <L extends DominoEventListener> L getListener(Class<L> listenerType) {
        return TestClientAppFactory.listenersRepository.getListener(listenerType);
    }

    @Override
    public TestDominoHistory history() {
        return (TestDominoHistory) make().getHistory();
    }

    @Override
    public void setRoutingListener(TestServerRouter.RoutingListener routingListener) {
        TestClientAppFactory.serverRouter.setRoutingListener(routingListener);
    }

    @Override
    public TestRoutingListener getDefaultRoutingListener() {
        return TestClientAppFactory.serverRouter.getDefaultRoutingListener();
    }

    @Override
    public void removeRoutingListener() {
        TestClientAppFactory.serverRouter.removeRoutingListener();
    }

    @Override
    public TestResponse forRequest(String requestKey) {
        return new TestResponse(requestKey);
    }

    @Override
    public TestResponse forRequest(Class<? extends ServerRequest> request) {
        return forRequest(request.getCanonicalName());
    }

    public void onRequestSuccessCompleted(Class<? extends ServerRequest> request, RequestCompleteHandler completeHandler) {
        TestClientAppFactory.serverRouter.addRequestSuccessCompleteHandler(request, completeHandler);
    }

    public void onRequestFailedCompleted(Class<? extends ServerRequest> request, RequestCompleteHandler completeHandler) {
        TestClientAppFactory.serverRouter.addRequestFailCompleteHandler(request, completeHandler);
    }

    @Override
    public Vertx vertx() {
        return vertx;
    }

    @Override
    public VertxEntryPointContext vertxEntryPointContext() {
        return testEntryPointContext;
    }

    @Override
    public FakeDominoOptions getDominoOptions() {
        return TestClientAppFactory.dominoOptions;
    }

    @Override
    public void registerStore(String key, Object data) {
        StoreRegistry.INSTANCE.registerStore(key, new Store<>(data));
    }

    public void setTestContext(TestContext testContext) {
        this.testContext = testContext;
    }

    private static class ListenerOf {
        private final Class<? extends DominoEventListener> listenerName;
        private final ListenerHandler handler;

        private ListenerOf(Class<? extends DominoEventListener> listenerName, ListenerHandler handler) {
            this.listenerName = listenerName;
            this.handler = handler;
        }
    }

    public static class TestResponse {

        private String request;
        private TestFailedResponseBean failedResponseBean;


        private TestResponse(String request) {
            this.request = request;
            this.failedResponseBean = new TestFailedResponseBean();
        }

        public void returnResponse(ResponseBean response) {
            TestClientAppFactory.serverRouter.fakeResponse(request, new TestServerRouter.SuccessReply(response));
        }

        public TestResponse failStatusCode(int status){
            failedResponseBean.setStatusCode(status);
            return this;
        }

        public TestResponse failStatusText(String statusText){
            failedResponseBean.setStatusText(statusText);
            return this;
        }

        public TestResponse failHeaders(Map<String, String> headers){
            failedResponseBean.setHeaders(headers);
            return this;
        }

        public TestResponse failBody(String body){
            failedResponseBean.setBody(body);
            return this;
        }

        public TestResponse failError(Throwable error){
            failedResponseBean.setThrowable(error);
            return this;
        }

        public void thenFail(){
            TestClientAppFactory.serverRouter.fakeResponse(request, new TestServerRouter.FailedReply(this.failedResponseBean));
        }

        public void failWith(Exception error) {
            this.failedResponseBean.setThrowable(error);
            TestClientAppFactory.serverRouter.fakeResponse(request, new TestServerRouter.FailedReply(this.failedResponseBean));
        }
    }


    @FunctionalInterface
    public interface ConfigOverrideHandler {
        void overrideConfig();
    }

    @FunctionalInterface
    public interface StartCompletedHandler {
        void onStarted();
    }

    @FunctionalInterface
    public interface RequestCompleteHandler{
        void onCompleted();
    }
}
