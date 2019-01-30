package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.client.commons.extensions.InMemoryDominoEventsListenerRepository;
import org.dominokit.domino.client.commons.request.ClientRouter;
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
                .eventsListenersRepository(new InMemoryDominoEventsListenerRepository())
                .history(new DesktopStateHistory())
                .asyncRunner(AsyncRunner.AsyncTask::onSuccess)
                .dominoOptions(new DesktopDominoOptions())
                .build();
    }

}
