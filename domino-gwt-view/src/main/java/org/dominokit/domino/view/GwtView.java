package org.dominokit.domino.view;

import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.view.slots.BodyElementSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GwtView {
    public static final Logger LOGGER = LoggerFactory.getLogger(GwtView.class);

    public static void init() {
        LOGGER.info("[document-body] slot registered");
        SlotRegistry.registerSlot("document-body", BodyElementSlot.create());
    }
}
