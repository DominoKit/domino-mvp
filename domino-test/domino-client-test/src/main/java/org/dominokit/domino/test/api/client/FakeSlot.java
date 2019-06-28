package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.shared.extension.Content;

import static java.util.Objects.nonNull;

public class FakeSlot implements IsSlot<ContentView> {

    private boolean revealed;
    private Content<FakeElement> old;

    @Override
    public void updateContent(ContentView view, HasContent.CreateHandler createHandler) {
        if (view.getContent(createHandler).get() instanceof FakeElement) {
            if (nonNull(old)) {
                old.get().remove();
            }

            this.old = view.getContent(createHandler);
            ((FakeElement) view.getContent(createHandler).get()).append();
        }
        revealed = true;
    }

    public boolean isRevealed() {
        return revealed;
    }
}
