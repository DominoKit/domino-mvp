package org.dominokit.domino.view.slots;

import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ModalView;

public class ModalIsSlot implements IsSlot<ModalView> {

    public static ModalIsSlot create(){
        return new ModalIsSlot();
    }

    @Override
    public void updateContent(ModalView view) {
        view.getContent();
        view.open();
    }
}
