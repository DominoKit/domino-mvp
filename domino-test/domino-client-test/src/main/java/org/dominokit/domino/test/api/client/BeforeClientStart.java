package org.dominokit.domino.test.api.client;

@FunctionalInterface
public interface BeforeClientStart {
    void onBeforeStart(ClientContext context);
}
