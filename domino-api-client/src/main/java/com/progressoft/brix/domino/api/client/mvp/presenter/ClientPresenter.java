package com.progressoft.brix.domino.api.client.mvp.presenter;

import com.progressoft.brix.domino.api.client.mvp.view.View;

public interface ClientPresenter<V extends View>{
    default void initView(V view){}
    ClientPresenter<V> prepare();
}
