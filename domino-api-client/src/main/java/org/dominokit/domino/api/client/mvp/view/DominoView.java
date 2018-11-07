package org.dominokit.domino.api.client.mvp.view;

public interface DominoView<T> extends View {

    boolean isInitialized();
    boolean isSingleton();
    T createRoot();
    void clear();

    default void setRevealHandler(RevealedHandler revealHandler){}
    default void setRemoveHandler(RemovedHandler removeHandler){}

    @FunctionalInterface
    interface RevealedHandler {
        void onRevealed();
    }

    @FunctionalInterface
    interface RemovedHandler {
        void onRemoved();
    }

}
