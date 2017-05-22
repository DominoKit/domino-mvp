package com.progressoft.brix.domino.api.client.mvp.presenter;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.extension.Contributions;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.api.shared.extension.ExtensionPoint;
import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;

public abstract class BaseClientPresenter<V extends View> implements ClientPresenter<V>{

    private static final CoreLogger LOGGER= CoreLoggerFactory.getLogger(BaseClientPresenter.class);

    private final PresenterState initialized = () ->
        LOGGER.info("Presenter "+BaseClientPresenter.this.getClass()+" Have already initialized.");


    private final PresenterState uninitialized = ()-> {
        view=loadView();
        initView(BaseClientPresenter.this.view);
        state= initialized;
    };

    private PresenterState state;

    protected V view;

    public BaseClientPresenter() {
        this.state = uninitialized;
        process();
    }

    @Override
    public ClientPresenter<V> process() {
        state.process();
        return this;
    }

    private V loadView(){
        return (V) ClientApp.make().getViewsRepository().getView(getName());
    }

    protected String getConcrete(){
        return this.getClass().getCanonicalName();
    }
    protected void applyContributions(Class<? extends ExtensionPoint> extensionPointInterface, ExtensionPoint extensionPoint){
        Contributions.apply(extensionPointInterface, extensionPoint);
    }

    private String getName(){
        return ClientApp.make().getPresentersRepository().getNameFromConcreteName(getConcrete());
    }
}
