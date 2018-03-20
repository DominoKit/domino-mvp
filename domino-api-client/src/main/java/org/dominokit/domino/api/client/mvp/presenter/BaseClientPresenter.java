package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.extension.Contributions;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.shared.extension.ExtensionPoint;
import org.dominokit.domino.api.shared.history.DominoHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseClientPresenter<V extends View> extends ClientPresenter<V> implements Presentable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseClientPresenter.class);

    private final PresenterState initialized = () ->
            LOGGER.info("Presenter " + BaseClientPresenter.this.getClass() + " Have already been initialized.");


    private final PresenterState uninitialized = () -> {
        view = loadView();
        initView(BaseClientPresenter.this.view);
        state = initialized;
    };

    private PresenterState state;

    protected V view;

    @Override
    public Presentable init() {
        this.state = uninitialized;
        prepare();
        return this;
    }

    @Override
    public ClientPresenter<V> prepare() {
        state.process();
        return this;
    }

    private V loadView() {
        return (V) ClientApp.make().getViewsRepository().getView(getName());
    }

    protected String getConcrete() {
        return this.getClass().getCanonicalName();
    }

    protected <E extends ExtensionPoint> void applyContributions(Class<E> extensionPointInterface, E extensionPoint) {
        Contributions.apply(extensionPointInterface, extensionPoint);
    }

    protected void runAsync(AsyncRunner.AsyncTask asyncTask) {
        ClientApp.make().getAsyncRunner().runAsync(asyncTask);
    }

    private String getName() {
        return ClientApp.make().getPresentersRepository().getNameFromConcreteName(getConcrete());
    }

    protected DominoHistory history(){
        return ClientApp.make().getHistory();
    }
}
