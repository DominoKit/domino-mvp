package org.dominokit.domino.test.api.client.annotations;

import org.dominokit.domino.api.client.mvp.presenter.BaseClientPresenter;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PresenterSpy {

    Class<? extends BaseClientPresenter> value();
}
