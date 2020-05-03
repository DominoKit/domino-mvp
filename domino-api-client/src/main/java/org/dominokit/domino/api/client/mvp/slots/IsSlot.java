package org.dominokit.domino.api.client.mvp.slots;

public interface IsSlot<T> {
    void updateContent(T view);
    default void cleanUp(){}
}
