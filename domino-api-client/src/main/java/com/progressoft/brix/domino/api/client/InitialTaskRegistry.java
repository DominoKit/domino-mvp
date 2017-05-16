package com.progressoft.brix.domino.api.client;

@FunctionalInterface
public interface InitialTaskRegistry {
    void registerInitialTask(InitializeTask task);
}
