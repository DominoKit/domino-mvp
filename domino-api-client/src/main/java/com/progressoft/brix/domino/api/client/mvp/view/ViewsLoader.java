package com.progressoft.brix.domino.api.client.mvp.view;

@FunctionalInterface
public interface ViewsLoader {
    void load(ViewsRepository repository);
}
