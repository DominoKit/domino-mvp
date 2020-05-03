package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.gwt.client.slots.ElementSlot;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.elemento.IsElement;

public class AppendElementSlot extends ElementSlot {

    private DominoElement<HTMLElement> element;

    public static AppendElementSlot of(HTMLElement element) {
        return new AppendElementSlot(element);
    }

    public static AppendElementSlot of(IsElement element) {
        return new AppendElementSlot(element);
    }

    public AppendElementSlot(HTMLElement element) {
        this.element = DominoElement.of(element);
    }

    public AppendElementSlot(DominoElement<HTMLElement> element) {
        this.element = element;
    }

    public AppendElementSlot(IsElement<HTMLElement> element) {
        this.element = DominoElement.of(element);
    }

    @Override
    public void updateContent(HTMLElement view) {
        DominoElement.of(element)
                .appendChild(view);
    }

}
