package org.dominokit.domino.gwt.client.app;

//import com.google.gwt.core.client.GWT;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.client.commons.request.ClientRouter;
import org.dominokit.domino.gwt.client.async.GwtAsyncRunner;
import org.dominokit.domino.gwt.client.events.ClientEventFactory;
import org.dominokit.domino.gwt.client.events.ClientRequestGwtEvent;
import org.dominokit.domino.gwt.client.extensions.CustomEventsDominoEventsRepository;
import org.dominokit.domino.gwt.client.options.DefaultDominoOptions;
import org.dominokit.domino.gwt.client.slots.ElementsSlotsManager;
import org.dominokit.domino.gwt.history.StateHistory;
import org.dominokit.domino.rest.DominoRestConfig;
import org.dominokit.domino.rest.gwt.DominoSimpleEventsBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DominoGWT {

    private static final Logger LOGGER = LoggerFactory.getLogger(DominoGWT.class);

    private static final InitOptions DEFAULT_OPTIONS = new InitOptions("");

    private DominoGWT() {
    }

    public static void init() {
        init(DEFAULT_OPTIONS);
    }

    public static void init(InitOptions initOptions) {

        ClientRouter clientRouter = new ClientRouter(new ClientEventFactory());

        ((DominoSimpleEventsBus) DominoSimpleEventsBus.INSTANCE).addEvent(ClientRequestGwtEvent.CLIENT_REQUEST_EVENT_TYPE);

        DominoRestConfig.initDefaults();
        ClientApp.ClientAppBuilder
                .clientRouter(clientRouter)
                .eventsBus(DominoSimpleEventsBus.INSTANCE)
                .eventsListenersRepository(new CustomEventsDominoEventsRepository())
                .history(new DominoMvpHistory(initOptions.getRootPath()))
                .asyncRunner(new GwtAsyncRunner())
                .dominoOptions(new DefaultDominoOptions())
                .slotsManager(new ElementsSlotsManager())
                .build();
    }

    public static class DominoMvpHistory extends StateHistory {
        public DominoMvpHistory(String rootPath) {
            super(rootPath);
        }

        @Override
        public boolean isInformOnPopState() {
            return ClientApp.make().dominoOptions().isMainApp();
        }
    }

    public static class InitOptions {
        private final String rootPath;

        public InitOptions(String rootPath) {
            this.rootPath = rootPath;
        }

        public String getRootPath() {
            return rootPath;
        }
    }

}
