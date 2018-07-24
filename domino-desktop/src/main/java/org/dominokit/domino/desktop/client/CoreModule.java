package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.client.commons.extensions.CoreMainExtensionPoint;
import org.dominokit.domino.client.commons.extensions.InMemoryDominoEventsListenerRepository;
import org.dominokit.domino.client.commons.mvp.presenter.InMemoryPresentersRepository;
import org.dominokit.domino.client.commons.mvp.view.InMemoryViewRepository;
import org.dominokit.domino.client.commons.request.ClientRouter;
import org.dominokit.domino.client.commons.request.InMemoryCommandsRepository;
import org.dominokit.domino.client.commons.request.InMemoryRequestRestSendersRepository;
import org.dominokit.domino.client.commons.request.ServerRouter;
import org.dominokit.domino.desktop.client.events.DesktopClientEventFactory;
import org.dominokit.domino.desktop.client.events.DesktopEventBus;

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
                .eventsListenersRepository(new InMemoryDominoEventsListenerRepository())
                .requestSendersRepository(new InMemoryRequestRestSendersRepository())
                .history(new DesktopStateHistory())
                .asyncRunner(AsyncRunner.AsyncTask::onSuccess).mainExtensionPoint(new CoreMainExtensionPoint())
                .dominoOptions(new DesktopDominoOptions())
                .build();
    }

}
