package org.dominokit.domino.test.api.client;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.ModuleConfigurator;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.server.config.ServerConfiguration;
import org.dominokit.domino.api.server.config.ServerConfigurationLoader;
import org.dominokit.domino.api.server.config.VertxConfiguration;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.api.server.entrypoint.VertxEntryPointContext;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.service.discovery.VertxServiceDiscovery;
import org.dominokit.domino.test.api.DominoTestServer;

import java.util.*;

import static org.dominokit.domino.api.client.ClientApp.make;
import static org.easymock.EasyMock.createMock;

public class DominoTestClient
        implements CanCustomizeClient, CanStartClient,
        ClientContext {

    private ModuleConfiguration[] modules;
    private Map<String, Presentable> presentersReplacements = new HashMap<>();
    private Map<String, View> viewsReplacements = new HashMap<>();
    private List<ViewOf> viewsOf = new ArrayList<>();
    private List<ListenerOf> listenersOf = new ArrayList<>();

    private VertxEntryPointContext testEntryPointContext;
    private Vertx vertx = Vertx.vertx();
    private BeforeClientStart beforeClientStart = context -> {
    };
    private StartCompleted startCompleted = context -> {
    };
    private DominoTestServer.AfterLoadHandler afterLoadHandler = context -> {
    };
    private boolean withServer = false;
    private TestContext testContext;

    private DominoTestClient(ModuleConfiguration... configurations) {
        this.modules = configurations;
    }

    public static CanCustomizeClient useModules(ModuleConfiguration... configurations) {
        return new DominoTestClient(configurations);
    }

    @Override
    public CanCustomizeClient replacePresenter(Class<? extends Presentable> original, Presentable presentable) {
        presentersReplacements.put(original.getCanonicalName(), presentable);
        return this;
    }

    @Override
    public CanCustomizeClient replaceView(Class<? extends Presentable> presenter, View view) {
        viewsReplacements.put(presenter.getCanonicalName(), view);
        return this;
    }

    @Override
    public CanCustomizeClient viewOf(Class<? extends Presentable> presenter, ViewHandler handler) {
        this.viewsOf.add(new ViewOf(presenter.getCanonicalName(), handler));
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
    public void start() {
        ServerConfiguration testServerConfiguration = new VertxConfiguration(new JsonObject());
        testEntryPointContext = new VertxEntryPointContext(createMock(RoutingContext.class), testServerConfiguration,
                vertx);
        VertxContext vertxContext = VertxContext.VertxContextBuilder.vertx(vertx)
                .router(Router.router(vertx))
                .serverConfiguration(testServerConfiguration)
                .vertxServiceDiscovery(new VertxServiceDiscovery(vertx))
                .configRetriever(ConfigRetriever.create(vertx))
                .build();
        new ServerConfigurationLoader(vertxContext).loadModules();

        init(testEntryPointContext);
        Arrays.stream(modules).forEach(this::configureModule);
        presentersReplacements.forEach((key, presentable) -> replacePresenter(key, () -> presentable));

        viewsReplacements.forEach((key, value) -> replaceView(key, () -> value));

        viewsOf.forEach(v -> v.handler.handle(getView(v.presenterName)));
        listenersOf.forEach(c -> c.handler.handle(getListener(c.listenerName)));

        if (withServer) {
            Async async = testContext.async();
            DominoTestServer.vertx(vertx())
                    .onAfterLoad(serverContext -> {
                        getDominoOptions().setPort(serverContext.getActualPort());
                        afterLoadHandler.handle(serverContext);
                        doStart(() -> {
                            startCompleted.onStarted(this);
                            async.complete();
                        });
                    })
                    .start();
        } else {
            doStart(() -> startCompleted.onStarted(this));
        }
    }

    private void doStart(ApplicationStartHandler applicationStartHandler) {
        beforeClientStart.onBeforeStart(this);
        getDominoOptions().setApplicationStartHandler(applicationStartHandler);
        make().run();
    }

    private void init(ServerEntryPointContext entryPointContext) {
        TestClientAppFactory.make(entryPointContext);
    }

    private void configureModule(ModuleConfiguration configuration) {
        new ModuleConfigurator().configureModule(configuration);
    }

    private void replacePresenter(String presenterName, TestPresenterFactory presenterFactory) {
        ((TestInMemoryPresenterRepository) make().getPresentersRepository())
                .replacePresenter(presenterName, presenterFactory);
    }

    private void replaceView(String presenterName, TestViewFactory viewFactory) {
        ((TestInMemoryViewRepository) make().getViewsRepository()).replaceView(presenterName, viewFactory);
    }

    private <T extends View> T getView(String presenterName) {
        return (T) make().getViewsRepository().getView(presenterName);
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

    private static class ViewOf {
        private final String presenterName;
        private final ViewHandler handler;

        private ViewOf(String presenterName, ViewHandler handler) {
            this.presenterName = presenterName;
            this.handler = handler;
        }
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

        private TestResponse(String request) {
            this.request = request;
        }

        public void returnResponse(ResponseBean response) {
            TestClientAppFactory.serverRouter.fakeResponse(request, new TestServerRouter.SuccessReply(response));
        }

        public void failWith(Exception error) {
            TestClientAppFactory.serverRouter.fakeResponse(request, new TestServerRouter.FailedReply(error));
        }
    }

}
