package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;

@FunctionalInterface
public interface ReplacePresenterHandler{
    void onReplacePresenter(Presentable presentable);
}