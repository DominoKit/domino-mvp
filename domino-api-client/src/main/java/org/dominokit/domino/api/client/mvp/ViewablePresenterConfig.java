package org.dominokit.domino.api.client.mvp;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.view.View;

import java.util.function.Supplier;

public interface ViewablePresenterConfig<P extends Presentable, V extends View> extends PresenterConfig<P> {
    Supplier<V> getViewSupplier();
}
