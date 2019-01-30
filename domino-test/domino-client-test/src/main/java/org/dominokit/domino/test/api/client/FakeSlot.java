package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.slots.Slot;
import org.dominokit.domino.api.shared.extension.Content;

import static java.util.Objects.nonNull;

public class FakeSlot implements Slot {

    private Content<FakeElement> old;

    @Override
    public void updateContent(Content content) {
        if(content.get() instanceof FakeElement){
            if(nonNull(old)){
                old.get().remove();
            }

            this.old = content;
            ((FakeElement) content.get()).append();
        }
    }
}
