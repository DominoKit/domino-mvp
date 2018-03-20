package org.dominokit.domino.test.api.client;

@FunctionalInterface
public interface BeforeStarted {
    void onBeforeStart(ClientContext context);
}
