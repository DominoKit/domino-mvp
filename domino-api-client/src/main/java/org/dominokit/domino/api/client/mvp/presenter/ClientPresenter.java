package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.mvp.view.View;

public abstract class ClientPresenter<V extends View> {

    protected  abstract ClientPresenter<V> prepare();
}
