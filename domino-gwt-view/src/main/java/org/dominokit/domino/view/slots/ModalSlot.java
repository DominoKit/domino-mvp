package org.dominokit.domino.view.slots;

import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ModalView;

public class ModalSlot implements IsSlot<ModalView> {

    public static ModalSlot create(){
        return new ModalSlot();
    }

    @Override
    public void updateContent(ModalView view) {
        view.getContent();
        view.open();
    }
}
