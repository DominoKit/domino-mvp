package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLBodyElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.presenter.BaseClientPresenter;
import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.client.mvp.slots.SlotRegistry;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.ui.utils.DominoElement;
import org.jboss.gwt.elemento.core.IsElement;

import static java.util.Objects.nonNull;

public class BodyElementSlot implements Slot {

    private DominoElement<HTMLBodyElement> body = DominoElement.body();

    private static final BodyElementSlot INSTANCE = new BodyElementSlot();

    private Content currentContent;

    public static BodyElementSlot create(){
        return INSTANCE;
    }

    private BodyElementSlot(){}

    @Override
    public void updateContent(Content content) {
        if(nonNull(currentContent)){
            HTMLElement contentElement = Js.uncheckedCast(content.get());
            DominoElement.of(contentElement)
                    .remove();
        }
        currentContent = content;
        body.appendChild(Js.<HTMLElement>uncheckedCast(content.get()));
    }
}
