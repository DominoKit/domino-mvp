package org.dominokit.domino.api.client.mvp;

import org.dominokit.domino.api.client.mvp.presenter.PresenterLoader;

@FunctionalInterface
public interface PresenterRegistry{
    void registerPresenter(PresenterLoader presenterLoader);
}