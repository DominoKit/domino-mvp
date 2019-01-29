package org.dominokit.domino.api.client;

public interface ModuleConfiguration {

    default void registerPresenters() {
        // Default implementation
    }

    default void registerViews() {
        // Default implementation
    }

    default void registerInitialTasks(InitialTaskRegistry registry) {
        // Default implementation
    }
}
