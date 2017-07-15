package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;

public interface CanUseModule {
    CanCustomizeClient replacePresenter(Class<? extends Presentable> original, Presentable replacement, ReplacePresenterHandler handler);
}
