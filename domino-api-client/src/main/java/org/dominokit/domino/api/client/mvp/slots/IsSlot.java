package org.dominokit.domino.api.client.mvp.slots;

import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.client.mvp.view.View;

public interface IsSlot<T extends View> {
    default void updateContent(T view){
        updateContent(view, () -> {});
    }
    void updateContent(T view, HasContent.CreateHandler createHandler);
    default void cleanUp(){}
}
