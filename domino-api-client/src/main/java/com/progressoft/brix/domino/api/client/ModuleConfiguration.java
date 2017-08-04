package com.progressoft.brix.domino.api.client;

import com.progressoft.brix.domino.api.client.extension.ContributionsRegistry;
import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.ViewRegistry;
import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import com.progressoft.brix.domino.api.client.request.RequestRestSendersRegistry;

public interface ModuleConfiguration {

    default void registerPresenters(PresenterRegistry registry) {
        // Default implementation
    }

    default void registerRequests(RequestRegistry registry) {
        // Default implementation
    }

    default void registerViews(ViewRegistry registry) {
        // Default implementation
    }

    default void registerContributions(ContributionsRegistry registry) {
        // Default implementation
    }

    default void registerInitialTasks(InitialTaskRegistry registry) {
        // Default implementation
    }

    default void registerRequestRestSenders(RequestRestSendersRegistry registry) {
        // Default implementation
    }
}
