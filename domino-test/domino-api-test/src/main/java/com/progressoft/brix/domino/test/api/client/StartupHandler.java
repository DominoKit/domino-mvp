package com.progressoft.brix.domino.test.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupHandler {
    private static final Logger LOGGER= LoggerFactory.getLogger(StartupHandler.class);

    private final StartCompleted START_COMPLETED= context -> LOGGER.info("Client started...");
    private final BeforeStarted BEFORE_RUN= context -> LOGGER.info("Starting client...");
    final BeforeStarted beforeRunHandler;
    final StartCompleted startCompleted;

    public StartupHandler() {
        this.beforeRunHandler = BEFORE_RUN;
        this.startCompleted = START_COMPLETED;
    }

    public StartupHandler(BeforeStarted beforeRunHandler,
                          StartCompleted startCompleted) {
        this.beforeRunHandler = beforeRunHandler;
        this.startCompleted = startCompleted;
    }

    public StartupHandler(StartCompleted startCompleted) {
        this.beforeRunHandler = BEFORE_RUN;
        this.startCompleted = startCompleted;
    }

}
