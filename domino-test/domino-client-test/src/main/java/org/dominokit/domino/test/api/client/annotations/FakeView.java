package org.dominokit.domino.test.api.client.annotations;

import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FakeView {

    Class<? extends ViewBaseClientPresenter> value();
}
