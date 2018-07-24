package org.dominokit.domino.api.client;

import org.dominokit.domino.api.client.extension.DominoEventsRegistry;
import org.dominokit.domino.api.client.mvp.PresenterRegistry;
import org.dominokit.domino.api.client.mvp.ViewRegistry;
import org.dominokit.domino.api.client.request.CommandRegistry;
import org.dominokit.domino.api.client.request.RequestRestSendersRegistry;

public interface ModuleConfiguration {

    default void registerPresenters(PresenterRegistry registry) {
        // Default implementation
    }

    default void registerRequests(CommandRegistry registry) {
        // Default implementation
    }

    default void registerViews(ViewRegistry registry) {
        // Default implementation
    }

    default void registerListeners(DominoEventsRegistry registry) {
        // Default implementation
    }

    default void registerInitialTasks(InitialTaskRegistry registry) {
        // Default implementation
    }

    default void registerRequestRestSenders(RequestRestSendersRegistry registry) {
        // Default implementation
    }
}
