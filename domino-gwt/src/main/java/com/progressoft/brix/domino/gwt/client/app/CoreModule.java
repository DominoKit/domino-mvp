package com.progressoft.brix.domino.gwt.client.app;

import com.google.gwt.user.client.History;
import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.history.PathTokenConstructor;
import com.progressoft.brix.domino.gwt.client.async.GwtAsyncRunner;
import com.progressoft.brix.domino.gwt.client.events.ClientEventFactory;
import com.progressoft.brix.domino.gwt.client.events.RequestEventProcessor;
import com.progressoft.brix.domino.gwt.client.events.ServerEventFactory;
import com.progressoft.brix.domino.gwt.client.events.SimpleEventsBus;
import com.progressoft.brix.domino.gwt.client.extensions.CoreMainExtensionPoint;
import com.progressoft.brix.domino.gwt.client.extensions.InMemoryContributionRepository;
import com.progressoft.brix.domino.gwt.client.history.GwtUrlHistory;
import com.progressoft.brix.domino.gwt.client.history.HistoryChangeHandler;
import com.progressoft.brix.domino.gwt.client.history.InMemoryPathToRequestMappersRepository;
import com.progressoft.brix.domino.gwt.client.mvp.presenter.InMemoryPresentersRepository;
import com.progressoft.brix.domino.gwt.client.mvp.view.InMemoryViewRepository;
import com.progressoft.brix.domino.gwt.client.request.ClientRouter;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestRestSendersRepository;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestsRepository;
import com.progressoft.brix.domino.gwt.client.request.ServerRouter;

public class CoreModule {

    private CoreModule(){

    }

    public static void init() {
        ClientRouter clientRouter = new ClientRouter(new ClientEventFactory());
        ServerRouter serverRouter = new ServerRouter(new ServerEventFactory());

        RequestEventProcessor requestEventProcessor = new RequestEventProcessor();
        SimpleEventsBus eventBus = new SimpleEventsBus(requestEventProcessor);

        ClientApp clientApp = ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .requestRepository(new InMemoryRequestsRepository())
                .presentersRepository(new InMemoryPresentersRepository())
                .viewsRepository(new InMemoryViewRepository())
                .contributionsRepository(new InMemoryContributionRepository())
                .pathToRequestMapperRepository(new InMemoryPathToRequestMappersRepository())
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .tokenConstruct(new PathTokenConstructor())
                .urlHistory(new GwtUrlHistory())
                .asyncRunner(new GwtAsyncRunner())
                .mainExtensionPoint(new CoreMainExtensionPoint())
                .build();

        History.addValueChangeHandler(new HistoryChangeHandler(clientApp.getPathToRequestMappersRepository(),
                clientApp.getTokenConstruct()));
    }
}
