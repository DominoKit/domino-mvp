package com.progressoft.brix.domino.test.api;


import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.history.PathTokenConstructor;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import com.progressoft.brix.domino.gwt.client.events.RequestEventProcessor;
import com.progressoft.brix.domino.gwt.client.history.InMemoryPathToRequestMappersRepository;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestRestSendersRepository;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestsRepository;

public class TestClientAppFactory {

    static TestInMemoryPresenterRepository presentersRepository;
    static InMemoryRequestsRepository requestRepository;
    static TestInMemoryViewRepository viewsRepository;
    static TestInMemoryContributionsRepository contributionsRepository;
    static InMemoryPathToRequestMappersRepository pathToRequestMappersRepository;

    private TestClientAppFactory() {
    }

    public static ClientApp make(ServerEntryPointContext entryPointContext) {

        TestClientRouter clientRouter = new TestClientRouter();
        TestServerRouter serverRouter = new TestServerRouter(entryPointContext);
        RequestEventProcessor requestEventProcessor = new RequestEventProcessor();
        TestEventBus eventBus = new TestEventBus(requestEventProcessor);

        presentersRepository = new TestInMemoryPresenterRepository();
        requestRepository = new InMemoryRequestsRepository();
        viewsRepository = new TestInMemoryViewRepository();
        contributionsRepository = new TestInMemoryContributionsRepository();
        pathToRequestMappersRepository = new InMemoryPathToRequestMappersRepository();
        return ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .requestRepository(requestRepository)
                .presentersRepository(presentersRepository)
                .viewsRepository(viewsRepository)
                .contributionsRepository(contributionsRepository)
                .pathToRequestMapperRepository(pathToRequestMappersRepository)
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .tokenConstruct(new PathTokenConstructor())
                .urlHistory(new TestUrlHistory())
                .mainExtensionPoint(TestMainContext::new)
                .build();
    }

    private static class TestMainContext implements MainContext {
    }
}
