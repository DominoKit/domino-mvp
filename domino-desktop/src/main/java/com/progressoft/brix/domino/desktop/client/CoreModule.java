package com.progressoft.brix.domino.desktop.client;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.async.AsyncRunner;
import com.progressoft.brix.domino.client.commons.extensions.CoreMainExtensionPoint;
import com.progressoft.brix.domino.client.commons.extensions.InMemoryContributionRepository;
import com.progressoft.brix.domino.client.commons.mvp.presenter.InMemoryPresentersRepository;
import com.progressoft.brix.domino.client.commons.mvp.view.InMemoryViewRepository;
import com.progressoft.brix.domino.client.commons.request.ClientRouter;
import com.progressoft.brix.domino.client.commons.request.InMemoryRequestRestSendersRepository;
import com.progressoft.brix.domino.client.commons.request.InMemoryCommandsRepository;
import com.progressoft.brix.domino.client.commons.request.ServerRouter;
import com.progressoft.brix.domino.desktop.client.events.DesktopClientEventFactory;
import com.progressoft.brix.domino.desktop.client.events.DesktopEventBus;

public class CoreModule {

    private CoreModule() {
    }

    public static void init() {
        ClientRouter clientRouter = new ClientRouter(new DesktopClientEventFactory());
        ServerRouter serverRouter = new ServerRouter(new DesktopRequestAsyncSender(new DesktopServerRequestEventFactory()));
        ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(new DesktopEventBus())
                .requestRepository(new InMemoryCommandsRepository())
                .presentersRepository(new InMemoryPresentersRepository())
                .viewsRepository(new InMemoryViewRepository())
                .contributionsRepository(new InMemoryContributionRepository())
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .history(new DesktopStateHistory())
                .asyncRunner(AsyncRunner.AsyncTask::onSuccess).mainExtensionPoint(new CoreMainExtensionPoint())
                .dominoOptions(new DesktopDominoOptions())
                .build();
    }

}
