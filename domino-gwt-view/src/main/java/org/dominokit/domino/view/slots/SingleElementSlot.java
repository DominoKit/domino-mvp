package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.gwt.client.slots.ElementSlot;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.elemento.IsElement;

public class SingleElementSlot extends ElementSlot {

    private DominoElement<HTMLElement> element;

    public static SingleElementSlot of(HTMLElement element) {
        return new SingleElementSlot(element);
    }

    public static SingleElementSlot of(IsElement element) {
        return new SingleElementSlot(element);
    }

    public SingleElementSlot(HTMLElement element) {
        this.element = DominoElement.of(element);
    }

    public SingleElementSlot(DominoElement<HTMLElement> element) {
        this.element = element;
    }

    public SingleElementSlot(IsElement<HTMLElement> element) {
        this.element = DominoElement.of(element);
    }

    @Override
    public void updateContent(HTMLElement view) {
        element
                .clearElement()
                .appendChild(view);
    }
}
