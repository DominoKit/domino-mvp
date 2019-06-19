package org.dominokit.domino.gwt.client.app;

import com.google.gwt.core.client.GWT;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.api.client.extension.InMemoryDominoEventsListenerRepository;
import org.dominokit.domino.client.commons.request.ClientRouter;
import org.dominokit.domino.gwt.client.async.GwtAsyncRunner;
import org.dominokit.domino.gwt.client.events.ClientEventFactory;
import org.dominokit.domino.gwt.client.events.ClientRequestGwtEvent;
import org.dominokit.domino.gwt.client.options.DefaultDominoOptions;
import org.dominokit.domino.gwt.history.StateHistory;
import org.dominokit.domino.rest.DominoRestConfig;
import org.dominokit.domino.rest.gwt.DominoSimpleEventsBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);

    private CoreModule() {
    }

    public static void init() {
        GWT.setUncaughtExceptionHandler(throwable -> LOGGER.error("Uncaught Exception", throwable));
        ClientRouter clientRouter = new ClientRouter(new ClientEventFactory());
        SlotRegistry.registerSlot("root", content -> {
            DomGlobal.document.body.appendChild(Js.<HTMLElement>cast(content.get()));
        });

        ((DominoSimpleEventsBus)DominoSimpleEventsBus.INSTANCE).addEvent(ClientRequestGwtEvent.CLIENT_REQUEST_EVENT_TYPE);

        DominoRestConfig.initDefaults();
        ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .eventsBus(DominoSimpleEventsBus.INSTANCE)
                .eventsListenersRepository(new InMemoryDominoEventsListenerRepository())
                .history(new StateHistory())
                .asyncRunner(new GwtAsyncRunner())
                .dominoOptions(new DefaultDominoOptions())
                .build();
    }
}
