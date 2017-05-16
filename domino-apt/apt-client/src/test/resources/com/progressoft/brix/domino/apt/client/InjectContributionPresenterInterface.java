package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.InjectContext;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;

public interface InjectContributionPresenterInterface extends Presentable {

    @InjectContext(extensionPoint = MainExtensionPoint.class)
    void onMainExtensionPointContextReceived(MainContext context);
}