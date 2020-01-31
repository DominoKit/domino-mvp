package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.elemento.IsElement;

public class AppendElementSlot implements IsSlot<ContentView> {

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
    public void updateContent(ContentView view, HasContent.CreateHandler createHandler) {
        DominoElement.of(element)
                .appendChild(Js.<HTMLElement>uncheckedCast(view.getContent(createHandler).get()));
    }
}
