package org.dominokit.domino.test.api.client;

@FunctionalInterface
public interface StartCompleted {
    void onStarted(ClientContext context);
}
