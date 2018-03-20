package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.InjectContext;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.shared.extension.MainContext;
import org.dominokit.domino.api.shared.extension.MainExtensionPoint;

public interface InjectContributionPresenterInterface extends Presentable {

    @InjectContext(extensionPoint = MainExtensionPoint.class)
    void onMainExtensionPointContextReceived(MainContext context);
}