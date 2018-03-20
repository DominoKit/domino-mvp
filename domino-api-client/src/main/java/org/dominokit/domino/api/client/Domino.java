package org.dominokit.domino.api.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Domino {

    private static final Logger LOGGER = Logger.getLogger(Domino.class.getName());

    public void onModuleLoad() {
        LOGGER.log(Level.INFO, "Domino");
    }
}