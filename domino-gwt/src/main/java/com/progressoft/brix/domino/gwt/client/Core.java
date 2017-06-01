package com.progressoft.brix.domino.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.progressoft.brix.domino.gwt.client.app.CoreModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Core implements EntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);

    @Override
    public void onModuleLoad() {
        CoreModule.init();
        LOGGER.info("Initialize domino module...");
    }
}
