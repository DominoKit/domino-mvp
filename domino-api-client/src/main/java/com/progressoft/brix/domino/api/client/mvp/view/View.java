package com.progressoft.brix.domino.api.client.mvp.view;

@FunctionalInterface
public interface View<T> {
    T get();
}
