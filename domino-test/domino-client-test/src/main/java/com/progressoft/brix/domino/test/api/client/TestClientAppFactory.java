package com.progressoft.brix.domino.test.api.client;


import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import com.progressoft.brix.domino.api.shared.history.DominoHistory;
import com.progressoft.brix.domino.client.commons.request.InMemoryRequestRestSendersRepository;
import com.progressoft.brix.domino.client.commons.request.InMemoryRequestsRepository;
import com.progressoft.brix.domino.gwt.client.events.RequestEventProcessor;

public class TestClientAppFactory {

    protected static TestInMemoryPresenterRepository presentersRepository;
    protected static InMemoryRequestsRepository requestRepository;
    protected static TestInMemoryViewRepository viewsRepository;
    protected static TestServerRouter serverRouter;
    protected static TestInMemoryContributionsRepository contributionsRepository;
    protected static TestDominoHistory history;
    protected static TestClientRouter clientRouter;
    protected static RequestEventProcessor requestEventProcessor;
    protected static TestEventBus eventBus;

    private TestClientAppFactory() {
    }

    public static ClientApp make(ServerEntryPointContext entryPointContext) {

        clientRouter = new TestClientRouter();
        serverRouter = new TestServerRouter(entryPointContext);
        requestEventProcessor = new RequestEventProcessor();
        eventBus = new TestEventBus(requestEventProcessor);

        presentersRepository = new TestInMemoryPresenterRepository();
        requestRepository = new InMemoryRequestsRepository();
        viewsRepository = new TestInMemoryViewRepository();
        contributionsRepository = new TestInMemoryContributionsRepository();
        history = new TestDominoHistory();

        return ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .requestRepository(requestRepository)
                .presentersRepository(presentersRepository)
                .viewsRepository(viewsRepository)
                .contributionsRepository(contributionsRepository)
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .history(history)
                .asyncRunner(new TestAsyncRunner())
                .mainExtensionPoint(TestMainContext::new)
                .dominoOptions(new FakeDominoOptions())
                .build();
    }

    private static class TestMainContext implements MainContext {
        @Override
        public DominoHistory history() {
            return history;
        }
    }
}
