package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.api.shared.extension.Contribution;

public interface CanCustomizeClient extends CanStartClient {
    CanCustomizeClient replacePresenter(Class<? extends Presentable> original, Presentable replacement);

    CanCustomizeClient replaceView(Class<? extends Presentable> presenter, View view);

    CanCustomizeClient viewOf(Class<? extends Presentable> presenter, ViewHandler handler);

    <C extends Contribution> CanCustomizeClient contributionOf(Class<C> contribution,
                                                               ContributionHandler<C> handler);

    CanCustomizeClient onBeforeStart(BeforeStarted beforeStarted);

    CanCustomizeClient onStartCompleted(StartCompleted startCompleted);
}
