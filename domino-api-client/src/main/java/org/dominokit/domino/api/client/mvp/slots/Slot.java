package org.dominokit.domino.api.client.mvp.slots;

import org.dominokit.domino.api.client.mvp.view.View;

public interface Slot<T extends View> {
    void updateContent(T view);
    default void cleanUp(){}
}
