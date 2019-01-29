package org.dominokit.domino.api.client.mvp.presenter;

import java.util.function.Supplier;

import static java.util.Objects.isNull;

public class PresenterSupplier<P extends Presentable> implements Supplier<P> {

    protected P presenter;
    protected final boolean singleton;
    protected Supplier<P> presenterFactory;

    public PresenterSupplier(boolean singleton, Supplier<P> presenterFactory) {
        this.singleton = singleton;
        this.presenterFactory = presenterFactory;
    }

    @Override
    public P get() {
        if (isNull(presenter) || !singleton) {
            presenter = presenterFactory.get();
            onBeforeInitPresenter();
            presenter.init();
        }
        return presenter;
    }

    protected void onBeforeInitPresenter() {

    }
}
