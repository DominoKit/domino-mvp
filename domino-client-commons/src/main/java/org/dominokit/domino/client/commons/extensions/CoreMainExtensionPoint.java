package org.dominokit.domino.client.commons.extensions;

import org.dominokit.domino.api.shared.extension.MainEventContext;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;

public class CoreMainExtensionPoint implements MainDominoEvent {
    @Override
    public MainEventContext context() {
        return new MainEventContext() {
        };
    }
}
