package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.shared.extension.MainEventContext;

public interface ListenerPresenterInterface extends Presentable {

    void onMainEventReceived(MainEventContext context);
}