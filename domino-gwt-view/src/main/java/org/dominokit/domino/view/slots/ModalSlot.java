package org.dominokit.domino.view.slots;

import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.client.mvp.view.ModalView;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.api.client.mvp.slots.ContentSlot;

public class ModalSlot implements ContentSlot {

    public static ModalSlot create(){
        return new ModalSlot();
    }

    @Override
    public void updateContent(HasContent view, HasContent.CreateHandler createHandler) {
        view.getContent(createHandler);
        ModalView modalView = Js.uncheckedCast(view);
        modalView.open();
    }

    @Override
    public void updateContent(Content view) {

    }
}
