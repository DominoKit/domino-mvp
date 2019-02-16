package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.presenter.ListenTo;
import org.dominokit.domino.api.client.annotations.presenter.Presenter;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.shared.extension.MainEventContext;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;

@Presenter
public class PresenterInterface implements Presentable {

    @ListenTo(event = MainDominoEvent.class)
    public void onMainDominoEventReceived(MainEventContext context){

    };

    @Override
    public Presentable init() {
        return this;
    }
}