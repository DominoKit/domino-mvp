package org.dominokit.domino.api.client.mvp.slots;

import org.dominokit.domino.api.client.mvp.view.View;

public interface IsSlot<T extends View> {
    void updateContent(T view);
    default void cleanUp(){}
}
