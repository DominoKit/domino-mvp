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
public class PresentersRegistrationsModuleConfiguration implements ModuleConfiguration {
    @Override
    public void registerPresenters(PresenterRegistry registry) {
        registry.registerPresenter(new PrototypePresenter(FirstAnnotatedClassWithPresenter.class.getCanonicalName(), FirstAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new FirstAnnotatedClassWithPresenter();
            }
        });
        registry.registerPresenter(new PrototypePresenter(SecondAnnotatedClassWithPresenter.class.getCanonicalName(), SecondAnnotatedClassWithPresenter.class.getCanonicalName()) {
            @Override
            protected Presentable make() {
                return new SecondAnnotatedClassWithPresenter();
            }
        });
    }
}