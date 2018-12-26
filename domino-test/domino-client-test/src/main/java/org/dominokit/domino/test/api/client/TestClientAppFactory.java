package org.dominokit.domino.test.api.client;


import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.shared.extension.MainEventContext;
import org.dominokit.domino.client.commons.request.InMemoryCommandsRepository;
import org.dominokit.domino.client.commons.request.InMemoryRequestRestSendersRepository;
import org.dominokit.domino.gwt.client.events.RequestEventProcessor;
import org.dominokit.domino.gwt.client.events.ServerEventFactory;
import org.dominokit.domino.gwt.client.request.GwtRequestAsyncSender;

public class TestClientAppFactory {

    protected static TestInMemoryPresenterRepository presentersRepository;
    protected static InMemoryCommandsRepository commandsRepository;
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

    public static ClientApp make(ServerEntryPointContext entryPointContext) {

        clientRouter = new TestClientRouter();
        serverRouter = new TestServerRouter(new GwtRequestAsyncSender(new ServerEventFactory()), entryPointContext);
        requestEventProcessor = new RequestEventProcessor();
        eventBus = new TestEventBus(requestEventProcessor);

        presentersRepository = new TestInMemoryPresenterRepository();
        commandsRepository = new InMemoryCommandsRepository();
        viewsRepository = new TestInMemoryViewRepository();
        listenersRepository = new TestInMemoryEventsListenersRepository();
        history = new TestDominoHistory();
        dominoOptions = new FakeDominoOptions();

        return ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .requestRepository(commandsRepository)
                .presentersRepository(presentersRepository)
                .viewsRepository(viewsRepository)
                .eventsListenersRepository(listenersRepository)
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .history(history)
                .asyncRunner(new TestAsyncRunner())
                .mainExtensionPoint(() -> new MainEventContext() {
                })
                .dominoOptions(dominoOptions)
                .build();
    }
}
