package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.gwt.elemento.core.IsElement;

public class SingleElementIsSlot implements IsSlot<ContentView> {

    private DominoElement<HTMLElement> element;

    public static SingleElementIsSlot of(HTMLElement element) {
        return new SingleElementIsSlot(element);
    }

    public static SingleElementIsSlot of(IsElement element) {
        return new SingleElementIsSlot(element);
    }

    public SingleElementIsSlot(HTMLElement element) {
        this.element = DominoElement.of(element);
    }

    public SingleElementIsSlot(DominoElement<HTMLElement> element) {
        this.element = element;
    }

    public SingleElementIsSlot(IsElement<HTMLElement> element) {
        this.element = DominoElement.of(element);
    }

    @Override
    public void updateContent(ContentView view) {
        DominoElement.of(element)
                .clearElement()
                .appendChild(Js.<HTMLElement>uncheckedCast(view.getContent().get()));
    }
}
