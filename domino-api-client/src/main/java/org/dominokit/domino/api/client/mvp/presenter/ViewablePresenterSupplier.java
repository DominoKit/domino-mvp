package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.mvp.view.View;

import java.util.function.Supplier;

public class ViewablePresenterSupplier<P extends ViewBaseClientPresenter<V>, V extends View> extends PresenterSupplier<P> {

    private Supplier<V> viewSupplier;

    public ViewablePresenterSupplier(boolean singleton, Supplier<P> presenterFactory) {
        super(singleton, presenterFactory);
    }

    @Override
    protected void onBeforeInitPresenter() {
        presenter.setViewSupplier(viewSupplier);
    }

    public void setViewSupplier(Supplier<V> viewSupplier) {
        this.viewSupplier = viewSupplier;
    }

}
