package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.api.shared.extension.Contribution;

public interface CanCustomizeClient extends CanStartClient {
    CanCustomizeClient replacePresenter(Class<? extends Presentable> original, Presentable replacement, ReplacePresenterHandler handler);

    CanCustomizeClient replaceView(Class<? extends Presentable> presenter, View view, ReplaceViewHandler handler);

    CanCustomizeClient viewOf(Class<? extends Presentable> presenter, ViewHandler handler);

    CanCustomizeClient contributionOf(Class<? extends Contribution> contribution,
                                      ContributionHandler handler);

    CanCustomizeClient onBeforeStart(BeforeStarted beforeStarted);

    CanCustomizeClient onStartCompleted(StartCompleted startCompleted);
}
