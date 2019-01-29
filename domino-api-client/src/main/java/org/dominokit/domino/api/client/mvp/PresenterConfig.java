package org.dominokit.domino.api.client.mvp;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;

import java.util.function.Supplier;

public interface PresenterConfig<P extends Presentable> {
    Supplier<P> getPresenterSupplier();
}
