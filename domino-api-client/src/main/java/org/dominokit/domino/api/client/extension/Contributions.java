package org.dominokit.domino.api.client.extension;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.extension.ExtensionPoint;

public class Contributions {

    private Contributions() {

    }

    public static <E extends ExtensionPoint> void apply(Class<E> extensionPointInterface, E extensionPoint) {
        ClientApp.make().applyContributions(extensionPointInterface, extensionPoint);
    }
}
