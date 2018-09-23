package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.mvp.ViewRegistry;
import org.dominokit.domino.api.client.mvp.view.PrototypeViewLoader;
import org.dominokit.domino.api.client.mvp.view.View;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class UiViewRegistrationModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerViews(ViewRegistry registry) {
        registry.registerView(new PrototypeViewLoader(DefaultAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected View make() {
                return new AnnotatedClassWithUiView();
            }
        });
    }
}