package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.shared.extension.MainContext;

public interface ContributionPresenterInterface extends Presentable {

    void onMainContextReceived(MainContext context);
}