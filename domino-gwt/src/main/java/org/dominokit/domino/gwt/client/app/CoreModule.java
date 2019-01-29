package org.dominokit.domino.gwt.client.app;

import com.google.gwt.core.client.GWT;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.client.commons.extensions.InMemoryDominoEventsListenerRepository;
import org.dominokit.domino.client.commons.mvp.view.InMemoryViewRepository;
import org.dominokit.domino.client.commons.request.ClientRouter;
import org.dominokit.domino.client.commons.request.ServerRouter;
import org.dominokit.domino.gwt.client.async.GwtAsyncRunner;
import org.dominokit.domino.gwt.client.events.ClientEventFactory;
import org.dominokit.domino.gwt.client.events.RequestEventProcessor;
import org.dominokit.domino.gwt.client.events.ServerEventFactory;
import org.dominokit.domino.gwt.client.events.SimpleEventsBus;
import org.dominokit.domino.gwt.client.history.StateHistory;
import org.dominokit.domino.gwt.client.options.DefaultDominoOptions;
import org.dominokit.domino.gwt.client.request.GwtRequestAsyncSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);

    private CoreModule() {
    }

    public static void init() {
        GWT.setUncaughtExceptionHandler(throwable -> LOGGER.error("Uncaught Exception", throwable));
        ClientRouter clientRouter = new ClientRouter(new ClientEventFactory());
        ServerRouter serverRouter = new ServerRouter(new GwtRequestAsyncSender(new ServerEventFactory()));
        RequestEventProcessor requestEventProcessor = new RequestEventProcessor();
        SimpleEventsBus eventBus = new SimpleEventsBus(requestEventProcessor);
        SlotRegistry.registerSlot("root", content -> {
            DomGlobal.document.body.appendChild(Js.<HTMLElement>cast(content.get()));
        });
        ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .serverRouter(serverRouter)
                .eventsBus(eventBus)
                .viewsRepository(new InMemoryViewRepository())
                .eventsListenersRepository(new InMemoryDominoEventsListenerRepository())
                .history(new StateHistory())
                .asyncRunner(new GwtAsyncRunner())
                .dominoOptions(new DefaultDominoOptions())
                .build();
    }
}
