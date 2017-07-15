package com.progressoft.brix.domino.test.api.client;

@FunctionalInterface
public interface BeforeStarted {
    void onBeforeStart(ClientContext context);
}
