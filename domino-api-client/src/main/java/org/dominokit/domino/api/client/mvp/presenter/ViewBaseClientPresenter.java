package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.view.DominoView;
import org.dominokit.domino.api.client.mvp.view.View;

public class ViewBaseClientPresenter<V extends View> extends BaseClientPresenter {

    protected V view;

    @Override
    protected void initialize() {
        view = loadView();
        if (view instanceof DominoView) {
            ((DominoView) view).setRevealHandler(onRevealed());
            ((DominoView) view).setRemoveHandler(onRemoved());
        }
        initView(ViewBaseClientPresenter.this.view);
        super.initialize();
    }

    protected void initView(V view) {
        // Default empty implementation
    }

    public DominoView.RevealedHandler onRevealed() {
        return null;
    }

    public DominoView.RemovedHandler onRemoved() {
        return null;
    }

    private V loadView() {
        return (V) ClientApp.make().getViewsRepository().getView(getName());
    }

}
