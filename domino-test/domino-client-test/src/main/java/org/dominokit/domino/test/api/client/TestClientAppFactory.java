package org.dominokit.domino.test.api.client;


import io.vertx.core.Vertx;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.rest.DominoRestConfig;
import org.dominokit.domino.rest.shared.EventProcessor;
import org.dominokit.domino.test.history.TestDominoHistory;

public class TestClientAppFactory {

    protected static TestServerRouter serverRouter;
    protected static TestInMemoryEventsListenersRepository listenersRepository;
    protected static TestDominoHistory history;
    protected static TestClientRouter clientRouter;
    protected static EventProcessor requestEventProcessor;
    protected static TestEventBus eventBus;
    protected static FakeDominoOptions dominoOptions;

    private TestClientAppFactory() {
    }

    public static ClientApp make(Vertx vertx) {

        clientRouter = new TestClientRouter();
        serverRouter = new TestServerRouter(vertx);
        requestEventProcessor = new EventProcessor();
        eventBus = new TestEventBus(requestEventProcessor);

        listenersRepository = new TestInMemoryEventsListenersRepository();
        history = new TestDominoHistory();
        dominoOptions = new FakeDominoOptions();
        DominoRestConfig.initDefaults();

        ClientApp clientApp = ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .eventsBus(eventBus)
                .eventsListenersRepository(listenersRepository)
                .history(history)
                .asyncRunner(new TestAsyncRunner())
                .dominoOptions(dominoOptions)
                .build();

        return clientApp;
    }
}
