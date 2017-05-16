package com.progressoft.brix.domino.api.client;

import com.progressoft.brix.domino.api.client.extension.ContributionsRegistry;
import com.progressoft.brix.domino.api.client.history.PathToRequestMapperRegistry;
import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.ViewRegistry;
import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import com.progressoft.brix.domino.api.client.request.RequestRestSendersRegistry;

public interface ModuleConfiguration {

    default void registerPresenters(PresenterRegistry registry) {
    }

    default void registerRequests(RequestRegistry registry) {
    }

    default void registerViews(ViewRegistry registry) {
    }

    default void registerContributions(ContributionsRegistry registry) {
    }

    default void registerInitialTasks(InitialTaskRegistry registry) {
    }

    default void registerPathMappers(PathToRequestMapperRegistry registry) {
    }

    default void registerRequestRestSenders(RequestRestSendersRegistry registry) {
    }
}
