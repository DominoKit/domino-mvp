package com.progressoft.brix.domino.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.progressoft.brix.domino.gwt.client.app.CoreModule;
import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;

public class Core implements EntryPoint {

    private static final CoreLogger LOGGER = CoreLoggerFactory.getLogger(Core.class);

    @Override
    public void onModuleLoad() {
        CoreModule.init();
        LOGGER.info("Initialize domino module...");
    }
}
