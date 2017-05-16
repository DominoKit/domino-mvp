package com.progressoft.brix.domino.api.client.mvp;

import com.progressoft.brix.domino.api.client.mvp.view.LazyViewLoader;

@FunctionalInterface
public interface ViewRegistry{
    void registerView(LazyViewLoader lazyViewLoader);
}