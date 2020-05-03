package org.dominokit.domino.view;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.extension.PredefinedSlots;
import org.dominokit.domino.view.slots.BodyElementSlot;
import org.dominokit.domino.view.slots.ModalSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GwtView {
    public static final Logger LOGGER = LoggerFactory.getLogger(GwtView.class);

    public static void init() {
        LOGGER.info("[body-slot] slot registered");
        ClientApp.make().slotsManager().registerSlot(PredefinedSlots.BODY_SLOT, BodyElementSlot.create());
        ClientApp.make().slotsManager().registerSlot(PredefinedSlots.MODAL_SLOT, ModalSlot.create());
    }
}
