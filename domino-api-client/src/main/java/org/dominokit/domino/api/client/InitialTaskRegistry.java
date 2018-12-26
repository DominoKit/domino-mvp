package org.dominokit.domino.api.client;

import org.dominokit.domino.api.client.startup.ClientStartupTask;

@FunctionalInterface
public interface InitialTaskRegistry {
    void registerInitialTask(ClientStartupTask task);
}
