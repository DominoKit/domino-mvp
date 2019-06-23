package org.dominokit.domino.api.client.mvp.view;

import org.dominokit.domino.api.client.mvp.slots.SlotsEntries;

public interface DominoView<T> extends ContentView {

    boolean isInitialized();

    void setInitialized(boolean initialized);

    boolean isSingleton();

    T createRoot();

    void clear();

    default void setRevealHandler(RevealedHandler revealHandler) {
    }

    default void setRemoveHandler(RemovedHandler removeHandler) {
    }

    @FunctionalInterface
    interface RevealedHandler {
        void onRevealed();
    }

    @FunctionalInterface
    interface RemovedHandler {
        void onRemoved();
    }

}
