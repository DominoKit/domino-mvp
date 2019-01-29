package org.dominokit.domino.test.api.client;


import io.vertx.ext.unit.TestContext;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.shared.extension.MainEventContext;
import org.dominokit.domino.gwt.client.events.RequestEventProcessor;

public class TestClientAppFactory {

    protected static TestInMemoryViewRepository viewsRepository;
    protected static TestServerRouter serverRouter;
    protected static TestInMemoryEventsListenersRepository listenersRepository;
    protected static TestDominoHistory history;
    protected static TestClientRouter clientRouter;
    protected static RequestEventProcessor requestEventProcessor;
    protected static TestEventBus eventBus;
    protected static FakeDominoOptions dominoOptions;

    private TestClientAppFactory() {
    }

    public static ClientApp make(ServerEntryPointContext entryPointContext, TestContext testContext) {

        clientRouter = new TestClientRouter();
        serverRouter = new TestServerRouter(entryPointContext, testContext);
        requestEventProcessor = new RequestEventProcessor();
        eventBus = new TestEventBus(requestEventProcessor);

        viewsRepository = new TestInMemoryViewRepository();
        listenersRepository = new TestInMemoryEventsListenersRepository();
        history = new TestDominoHistory();
        dominoOptions = new FakeDominoOptions();

        return ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .viewsRepository(viewsRepository)
                .eventsListenersRepository(listenersRepository)
                .history(history)
                .asyncRunner(new TestAsyncRunner())
                .dominoOptions(dominoOptions)
                .build();
    }
}
