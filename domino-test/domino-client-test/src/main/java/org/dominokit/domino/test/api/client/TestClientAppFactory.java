package org.dominokit.domino.test.api.client;


import io.vertx.core.Vertx;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.request.RequestContext;
import org.dominokit.domino.gwt.client.events.RequestEventProcessor;

public class TestClientAppFactory {

    protected static TestServerRouter serverRouter;
    protected static TestInMemoryEventsListenersRepository listenersRepository;
    protected static TestDominoHistory history;
    protected static TestClientRouter clientRouter;
    protected static RequestEventProcessor requestEventProcessor;
    protected static TestEventBus eventBus;
    protected static FakeDominoOptions dominoOptions;

    private TestClientAppFactory() {
    }

    public static ClientApp make(Vertx vertx) {

        clientRouter = new TestClientRouter();
        serverRouter = new TestServerRouter(vertx);
        requestEventProcessor = new RequestEventProcessor();
        eventBus = new TestEventBus(requestEventProcessor);

        listenersRepository = new TestInMemoryEventsListenersRepository();
        history = new TestDominoHistory();
        dominoOptions = new FakeDominoOptions();

        ClientApp clientApp = ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .eventsListenersRepository(listenersRepository)
                .history(history)
                .asyncRunner(new TestAsyncRunner())
                .dominoOptions(dominoOptions)
                .build();

        RequestContext.init(clientApp);
        return clientApp;
    }
}
