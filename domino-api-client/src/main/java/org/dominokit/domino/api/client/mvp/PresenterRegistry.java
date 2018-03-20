package org.dominokit.domino.api.client.mvp;

import org.dominokit.domino.api.client.mvp.presenter.LazyPresenterLoader;

@FunctionalInterface
public interface PresenterRegistry{
    void registerPresenter(LazyPresenterLoader lazyPresenterLoader);
}