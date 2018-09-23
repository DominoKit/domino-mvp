package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.ModuleConfiguration;
import org.dominokit.domino.api.client.mvp.PresenterRegistry;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.presenter.PrototypePresenter;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.module.client.ClientModuleAnnotationProcessor")
public class PresenterRegistrationModuleConfiguration implements ModuleConfiguration {

    @Override
    public void registerPresenters(PresenterRegistry registry) {
        registry.registerPresenter(new PrototypePresenter(DefaultAnnotatedClassWithPresenter.class.getCanonicalName(), DefaultAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new DefaultAnnotatedClassWithPresenter();
            }
        });
    }
}