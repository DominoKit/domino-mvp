package com.progressoft.brix.domino.test.api.client;

@FunctionalInterface
public interface BeforeRun {
    void onBeforeRun(ClientContext context);
}
