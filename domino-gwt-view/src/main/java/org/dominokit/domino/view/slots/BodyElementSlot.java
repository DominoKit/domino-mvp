package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLBodyElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.api.client.mvp.slots.ContentSlot;
import org.dominokit.domino.ui.utils.DominoElement;

import static java.util.Objects.nonNull;

public class BodyElementSlot implements ContentSlot {

    private DominoElement<HTMLBodyElement> body = DominoElement.body();

    private static final BodyElementSlot INSTANCE = new BodyElementSlot();

    private Content currentContent;

    public static BodyElementSlot create() {
        return INSTANCE;
    }

    private BodyElementSlot() {
    }

    @Override
    public void updateContent(Content view) {
        if (nonNull(currentContent)) {
            HTMLElement contentElement = Js.uncheckedCast(currentContent.get());
            DominoElement.of(contentElement)
                    .remove();
        }
        body.appendChild(Js.<HTMLElement>uncheckedCast(view.get()));
        currentContent = view;
    }

    @Override
    public void updateContent(HasContent view, HasContent.CreateHandler createHandler) {
        updateContent(view.getContent(createHandler));
    }

}
