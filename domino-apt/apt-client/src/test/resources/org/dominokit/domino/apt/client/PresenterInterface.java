package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.ListenTo;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.shared.extension.MainEventContext;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;

public interface PresenterInterface extends Presentable {

    @ListenTo(event = MainDominoEvent.class)
    void onMainDominoEventReceived(MainEventContext context);
}