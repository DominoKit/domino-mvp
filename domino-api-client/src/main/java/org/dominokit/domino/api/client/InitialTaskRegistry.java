package org.dominokit.domino.api.client;

@FunctionalInterface
public interface InitialTaskRegistry {
    void registerInitialTask(ClientStartupTask task);
}
