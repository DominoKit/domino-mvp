package com.progressoft.brix.domino.api.client.mvp.presenter;

import com.progressoft.brix.domino.api.client.mvp.view.View;

public abstract class ClientPresenter<V extends View> {
    protected void initView(V view) {
        // Default empty implementation
    }

    protected  abstract ClientPresenter<V> prepare();
}
