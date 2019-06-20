package org.dominokit.domino.view.slots;

import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.client.mvp.view.ModalView;

public class ModalSlot implements Slot<ModalView> {

    public static ModalSlot create(){
        return new ModalSlot();
    }

    @Override
    public void updateContent(ModalView view) {
        view.open();
    }
}
