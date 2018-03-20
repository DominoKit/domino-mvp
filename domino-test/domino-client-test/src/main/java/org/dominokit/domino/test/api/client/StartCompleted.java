package org.dominokit.domino.test.api.client;

import org.dominokit.domino.test.api.client.ClientContext;

@FunctionalInterface
public interface StartCompleted {
    void onStarted(ClientContext context);
}
