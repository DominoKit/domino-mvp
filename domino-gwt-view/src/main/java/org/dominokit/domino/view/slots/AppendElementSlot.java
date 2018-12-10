package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.gwt.elemento.core.IsElement;

public class AppendElementSlot implements Slot {

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
    public void updateContent(Content content) {
        DominoElement.of(element)
                .appendChild(Js.<HTMLElement>uncheckedCast(content.get()));
    }
}
