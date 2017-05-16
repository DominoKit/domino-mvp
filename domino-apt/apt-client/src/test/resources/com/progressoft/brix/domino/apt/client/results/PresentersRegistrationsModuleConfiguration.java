package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.apt.client.SecondAnnotatedClassWithPresenter;
import com.progressoft.brix.domino.apt.client.FirstPresenterInterface;
import com.progressoft.brix.domino.apt.client.SecondPresenterInterface;
import com.progressoft.brix.domino.apt.client.FirstAnnotatedClassWithPresenter;
import com.progressoft.brix.domino.api.client.mvp.presenter.LazyPresenterLoader;

public class PresentersRegistrationsModuleConfiguration implements ModuleConfiguration {
    @Override
    public void registerPresenters(PresenterRegistry registry) {
        registry.registerPresenter(new LazyPresenterLoader(FirstPresenterInterface.class.getCanonicalName(), FirstAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new FirstAnnotatedClassWithPresenter();
            }
        });
        registry.registerPresenter(new LazyPresenterLoader(SecondPresenterInterface.class.getCanonicalName(), SecondAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new SecondAnnotatedClassWithPresenter();
            }
        });
    }
}