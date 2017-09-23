package com.progressoft.brix.domino.desktop.client;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.client.commons.extensions.CoreMainExtensionPoint;
import com.progressoft.brix.domino.client.commons.extensions.InMemoryContributionRepository;
import com.progressoft.brix.domino.client.commons.mvp.presenter.InMemoryPresentersRepository;
import com.progressoft.brix.domino.client.commons.mvp.view.InMemoryViewRepository;
import com.progressoft.brix.domino.client.commons.request.ClientRouter;
import com.progressoft.brix.domino.client.commons.request.InMemoryRequestRestSendersRepository;
import com.progressoft.brix.domino.client.commons.request.InMemoryRequestsRepository;
import com.progressoft.brix.domino.client.commons.request.ServerRouter;
import com.progressoft.brix.domino.desktop.client.events.DesktopClientEventFactory;
import com.progressoft.brix.domino.desktop.client.events.DesktopEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);

    private CoreModule() {
    }

    public static void init() {
        ClientRouter clientRouter = new ClientRouter(new DesktopClientEventFactory());
        ServerRouter serverRouter = new ServerRouter(new DesktopRequestAsyncSender(new DesktopServerRequestEventFactory()));
        ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(new DesktopEventBus())
                .requestRepository(new InMemoryRequestsRepository())
                .presentersRepository(new InMemoryPresentersRepository())
                .viewsRepository(new InMemoryViewRepository())
                .contributionsRepository(new InMemoryContributionRepository())
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .history(new DesktopStateHistory())
                .asyncRunner(new DesktopAsyncRunner())
                .mainExtensionPoint(new CoreMainExtensionPoint())
                .dominoOptions(new DesktopDominoOptions())
                .build();
    }

}
