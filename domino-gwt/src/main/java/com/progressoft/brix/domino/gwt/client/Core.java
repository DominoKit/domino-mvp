package com.progressoft.brix.domino.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.progressoft.brix.domino.gwt.client.app.CoreModule;
import elemental2.dom.XMLHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Core implements EntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);

    @Override
    public void onModuleLoad() {
        XMLHttpRequest xhr = new XMLHttpRequest();
        CoreModule.init();
        LOGGER.info("Initialize domino module...");
    }
}
