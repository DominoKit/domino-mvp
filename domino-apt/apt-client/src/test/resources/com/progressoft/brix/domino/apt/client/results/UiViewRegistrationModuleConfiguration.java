package com.progressoft.brix.domino.apt.client;

import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.ModuleConfiguration;
import com.progressoft.brix.domino.api.client.mvp.ViewRegistry;
import com.progressoft.brix.domino.api.client.mvp.view.LazyViewLoader;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithUiView;
import com.progressoft.brix.domino.apt.client.PresenterInterface;

@AutoService(ModuleConfiguration.class)
public class UiViewRegistrationModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerViews(ViewRegistry registry) {
        registry.registerView(new LazyViewLoader(PresenterInterface.class.getCanonicalName()) {
            @Override
            protected View make() {
                return new AnnotatedClassWithUiView();
            }
        });
    }
}