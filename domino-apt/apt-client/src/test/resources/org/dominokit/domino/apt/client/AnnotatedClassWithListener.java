package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.Listener;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;

@Listener
public class AnnotatedClassWithListener implements DominoEventListener<MainDominoEvent> {

    @Override
    public void listen(MainDominoEvent dominoEvent) {
        //for code generation testing only
    }
}