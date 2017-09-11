package com.progressoft.brix.domino.api.client.extension;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.shared.extension.ExtensionPoint;

public class Contributions {

    private Contributions() {

    }

    public static <E extends ExtensionPoint> void apply(Class<E> extensionPointInterface, E extensionPoint) {
        ClientApp.make().applyContributions(extensionPointInterface, extensionPoint);
    }
}
