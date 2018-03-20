package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;

public interface CanUseModule {
    CanCustomizeClient replacePresenter(Class<? extends Presentable> original, Presentable replacement, ReplacePresenterHandler handler);
}
