package org.dominokit.domino.api.client.mvp;

import org.dominokit.domino.api.client.mvp.view.LazyViewLoader;

@FunctionalInterface
public interface ViewRegistry{
    void registerView(LazyViewLoader lazyViewLoader);
}