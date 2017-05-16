package com.progressoft.brix.domino.logger.client;

import java.util.logging.Level;

public class EngineLogger {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(EngineLogger.class.getName());

    public void onModuleLoad() {
        LOGGER.log(Level.INFO, "EngineLogger");
    }
}
