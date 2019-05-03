package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLBodyElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.ui.utils.DominoElement;

import static java.util.Objects.nonNull;

public class BodyElementSlot implements Slot {

    private DominoElement<HTMLBodyElement> body = DominoElement.body();

    private static final BodyElementSlot INSTANCE = new BodyElementSlot();


    public static BodyElementSlot create(){
        return INSTANCE;
    }

    private BodyElementSlot(){}

    @Override
    public void updateContent(Content content) {
        body.clearElement();
        body.appendChild(Js.<HTMLElement>uncheckedCast(content.get()));
    }
}
