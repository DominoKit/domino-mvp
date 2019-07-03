package org.dominokit.domino.view.slots;

import elemental2.dom.HTMLBodyElement;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.ui.utils.DominoElement;

import static java.util.Objects.nonNull;

public class BodyElementSlot implements IsSlot<ContentView> {

    private DominoElement<HTMLBodyElement> body = DominoElement.body();

    private static final BodyElementSlot INSTANCE = new BodyElementSlot();

    private Content currentContent;

    public static BodyElementSlot create() {
        return INSTANCE;
    }

    private BodyElementSlot() {
    }

    @Override
    public void updateContent(ContentView view, HasContent.CreateHandler createHandler) {
        if (nonNull(currentContent)) {
            HTMLElement contentElement = Js.uncheckedCast(currentContent.get());
            DominoElement.of(contentElement)
                    .remove();
        }
        Content content = view.getContent(createHandler);
        body.appendChild(Js.<HTMLElement>uncheckedCast(content.get()));
        currentContent = content;
    }
}
