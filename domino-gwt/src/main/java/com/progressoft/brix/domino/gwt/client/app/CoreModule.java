package com.progressoft.brix.domino.gwt.client.app;

import com.google.gwt.core.client.GWT;
import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.gwt.client.async.GwtAsyncRunner;
import com.progressoft.brix.domino.gwt.client.events.ClientEventFactory;
import com.progressoft.brix.domino.gwt.client.events.RequestEventProcessor;
import com.progressoft.brix.domino.gwt.client.events.ServerEventFactory;
import com.progressoft.brix.domino.gwt.client.events.SimpleEventsBus;
import com.progressoft.brix.domino.gwt.client.extensions.CoreMainExtensionPoint;
import com.progressoft.brix.domino.gwt.client.extensions.InMemoryContributionRepository;
import com.progressoft.brix.domino.gwt.client.history.StateHistory;
import com.progressoft.brix.domino.gwt.client.mvp.presenter.InMemoryPresentersRepository;
import com.progressoft.brix.domino.gwt.client.mvp.view.InMemoryViewRepository;
import com.progressoft.brix.domino.gwt.client.options.RestyGwtOptions;
import com.progressoft.brix.domino.gwt.client.request.ClientRouter;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestRestSendersRepository;
import com.progressoft.brix.domino.gwt.client.request.InMemoryRequestsRepository;
import com.progressoft.brix.domino.gwt.client.request.ServerRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);

    private CoreModule() {
    }

    public static void init() {
        GWT.setUncaughtExceptionHandler(throwable -> LOGGER.error("Uncaught Exception", throwable));
        ClientRouter clientRouter = new ClientRouter(new ClientEventFactory());
        ServerRouter serverRouter = new ServerRouter(new ServerEventFactory());
        RequestEventProcessor requestEventProcessor = new RequestEventProcessor();
        SimpleEventsBus eventBus = new SimpleEventsBus(requestEventProcessor);
        ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .requestRepository(new InMemoryRequestsRepository())
                .presentersRepository(new InMemoryPresentersRepository())
                .viewsRepository(new InMemoryViewRepository())
                .contributionsRepository(new InMemoryContributionRepository())
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .history(new StateHistory())
                .asyncRunner(new GwtAsyncRunner())
                .mainExtensionPoint(new CoreMainExtensionPoint())
                .dominoOptions(new RestyGwtOptions())
                .build();
    }
}
