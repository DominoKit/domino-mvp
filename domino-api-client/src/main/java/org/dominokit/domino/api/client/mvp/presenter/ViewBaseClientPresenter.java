package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.view.View;

public class ViewBaseClientPresenter<V extends View> extends BaseClientPresenter {

    protected V view;

    @Override
    protected void initialize() {
        view = loadView();
        initView(ViewBaseClientPresenter.this.view);
        super.initialize();
    }

    protected void initView(V view) {
        // Default empty implementation
    }

    private V loadView() {
        return (V) ClientApp.make().getViewsRepository().getView(getName());
    }

}
