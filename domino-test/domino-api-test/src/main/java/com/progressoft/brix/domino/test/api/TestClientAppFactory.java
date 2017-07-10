package com.progressoft.brix.domino.test.api;


import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import com.progressoft.brix.domino.api.shared.history.DominoHistory;
import com.progressoft.brix.domino.gwt.client.events.RequestEventProcessor;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestRestSendersRepository;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestsRepository;

public class TestClientAppFactory {

    static TestInMemoryPresenterRepository presentersRepository;
    static InMemoryRequestsRepository requestRepository;
    static TestInMemoryViewRepository viewsRepository;
    static TestServerRouter serverRouter;
    static TestInMemoryContributionsRepository contributionsRepository;
    static TestDominoHistory history;
    static TestClientRouter clientRouter;
    static RequestEventProcessor requestEventProcessor;
    static TestEventBus eventBus;

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
                .build();
    }

    private static class TestMainContext implements MainContext {
        @Override
        public DominoHistory history() {
            return history;
        }
    }
}
