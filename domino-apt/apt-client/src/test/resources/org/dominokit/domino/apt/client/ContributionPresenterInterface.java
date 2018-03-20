package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.shared.extension.MainContext;

public interface ContributionPresenterInterface extends Presentable {

    void onMainContextReceived(MainContext context);
}