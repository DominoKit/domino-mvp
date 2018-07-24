package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.annotations.Listener;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.inject.ListenToDominoEventProcessor")
@Listener
public class PresenterInterfaceListenerForMainDominoEvent implements DominoEventListener<MainDominoEvent> {

    @Override
    public void listen(MainDominoEvent event) {
        new PresenterInterfaceCommand()
                .onPresenterReady(presenter -> presenter.onMainDominoEventReceived(event.context()))
                .send();
    }
}