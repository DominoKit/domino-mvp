package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.gwt.elemento.core.IsElement;

public class AppendElementIsSlot implements IsSlot<ContentView> {

    private DominoElement<HTMLElement> element;

    public static AppendElementIsSlot of(HTMLElement element) {
        return new AppendElementIsSlot(element);
    }

    public static AppendElementIsSlot of(IsElement element) {
        return new AppendElementIsSlot(element);
    }

    public AppendElementIsSlot(HTMLElement element) {
        this.element = DominoElement.of(element);
    }

    public AppendElementIsSlot(DominoElement<HTMLElement> element) {
        this.element = element;
    }

    public AppendElementIsSlot(IsElement<HTMLElement> element) {
        this.element = DominoElement.of(element);
    }

    @Override
    public void updateContent(ContentView view) {
        DominoElement.of(element)
                .appendChild(Js.<HTMLElement>uncheckedCast(view.getContent().get()));
    }
}
