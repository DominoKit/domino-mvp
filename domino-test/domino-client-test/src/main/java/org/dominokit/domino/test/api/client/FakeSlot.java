package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.client.mvp.view.ContentView;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.shared.extension.Content;

import static java.util.Objects.nonNull;

public class FakeSlot implements Slot<ContentView> {

    private boolean revealed;
    private Content<FakeElement> old;

    @Override
    public void updateContent(ContentView view) {
        if (view.getContent().get() instanceof FakeElement) {
            if (nonNull(old)) {
                old.get().remove();
            }

            this.old = view.getContent();
            ((FakeElement) view.getContent().get()).append();
        }
        revealed = true;
    }

    public boolean isRevealed() {
        return revealed;
    }
}
