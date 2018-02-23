package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.presenter.LazyPresenterLoader;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class PresentersRegistrationsModuleConfiguration implements ModuleConfiguration {
    @Override
    public void registerPresenters(PresenterRegistry registry) {
        registry.registerPresenter(new LazyPresenterLoader(FirstAnnotatedClassWithPresenter.class.getCanonicalName(), FirstAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new FirstAnnotatedClassWithPresenter();
            }
        });
        registry.registerPresenter(new LazyPresenterLoader(SecondAnnotatedClassWithPresenter.class.getCanonicalName(), SecondAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new SecondAnnotatedClassWithPresenter();
            }
        });
    }
}