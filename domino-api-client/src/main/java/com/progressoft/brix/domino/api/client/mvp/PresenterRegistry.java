package com.progressoft.brix.domino.api.client.mvp;

import com.progressoft.brix.domino.api.client.mvp.presenter.LazyPresenterLoader;

@FunctionalInterface
public interface PresenterRegistry{
    void registerPresenter(LazyPresenterLoader lazyPresenterLoader);
}