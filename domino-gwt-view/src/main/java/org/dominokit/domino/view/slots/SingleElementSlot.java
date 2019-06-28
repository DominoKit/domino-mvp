package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.gwt.elemento.core.IsElement;

public class SingleElementSlot implements IsSlot<ContentView> {

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
    public void updateContent(ContentView view, HasContent.CreateHandler createHandler) {
        element.clearElement()
                .appendChild(Js.<HTMLElement>uncheckedCast(view.getContent(createHandler).get()));
    }
}
