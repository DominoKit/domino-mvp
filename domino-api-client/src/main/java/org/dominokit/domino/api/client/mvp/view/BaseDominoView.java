package org.dominokit.domino.api.client.mvp.view;

import org.dominokit.domino.api.shared.extension.Content;

import static java.util.Objects.nonNull;

public abstract class BaseDominoView<T> implements DominoView<T>, HasContent {

    private boolean initialized = false;
    protected RevealedHandler revealHandler;
    protected RemovedHandler removeHandler;

    private T root;

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public Content getContent() {
        return this.getContent(null);
    }

    @Override
    public Content getContent(CreateHandler createHandler) {
        if (!initialized || !isSingleton()) {
            root = init();
            initRoot(root);
            if (nonNull(createHandler)) {
                createHandler.onCreated();
            }
            initialized = true;
        }
        return (Content<T>) () -> root;
    }

    protected abstract void initRoot(T root);

    protected abstract T init();

    @Override
    public void clear() {

    }

    @Override
    public void setRevealHandler(RevealedHandler revealHandler) {
        this.revealHandler = revealHandler;
    }

    @Override
    public void setRemoveHandler(RemovedHandler removeHandler) {
        this.removeHandler = removeHandler;
    }
}
