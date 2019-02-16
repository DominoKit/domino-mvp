package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.presenter.Listener;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;

@Listener
public class AnnotatedClassWithPresentableListener implements DominoEventListener<MainDominoEvent> {

    @Override
    public void onEventReceived(MainDominoEvent dominoEvent) {
        //for generation testing only
    }
}