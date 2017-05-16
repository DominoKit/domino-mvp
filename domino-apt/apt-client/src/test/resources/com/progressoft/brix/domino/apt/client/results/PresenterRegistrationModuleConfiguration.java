package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.apt.client.PresenterInterface;
import com.progressoft.brix.domino.apt.client.DefaultAnnotatedClassWithPresenter;
import com.progressoft.brix.domino.api.client.mvp.presenter.LazyPresenterLoader;

public class PresenterRegistrationModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerPresenters(PresenterRegistry registry) {
        registry.registerPresenter(new LazyPresenterLoader(PresenterInterface.class.getCanonicalName(), DefaultAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new DefaultAnnotatedClassWithPresenter();
            }
        });
    }
}