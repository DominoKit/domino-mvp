package org.dominokit.domino.view;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.view.slots.BodyElementSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class View implements EntryPoint {

    public static final Logger LOGGER = LoggerFactory.getLogger(View.class);

    @Override
    public void onModuleLoad() {
        LOGGER.info("[document-body] slot registered");
        SlotRegistry.registerSlot("document-body", BodyElementSlot.create());
    }
}
