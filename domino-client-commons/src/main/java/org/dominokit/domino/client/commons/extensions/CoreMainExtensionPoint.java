package org.dominokit.domino.client.commons.extensions;

import org.dominokit.domino.api.shared.extension.MainContext;
import org.dominokit.domino.api.shared.extension.MainExtensionPoint;

public class CoreMainExtensionPoint implements MainExtensionPoint {
    @Override
    public MainContext context() {
        return new MainContext() {
        };
    }
}
