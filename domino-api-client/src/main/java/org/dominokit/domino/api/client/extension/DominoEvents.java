package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.extension.DominoEvent;

public class DominoEvents {

    private DominoEvents() {

    }

    public static <E extends DominoEvent> void fire(Class<E> eventType, E dominoEvent) {
        ClientApp.make().fireEvent(eventType, dominoEvent);
    }
}
