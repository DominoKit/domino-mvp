package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.extension.DominoEvents;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.history.DominoHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseClientPresenter extends ClientPresenter implements Presentable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseClientPresenter.class);

    private final PresenterState initialized = () ->
            LOGGER.info("Presenter " + BaseClientPresenter.this.getClass() + " Have already been initialized.");

    private final PresenterState uninitialized = this::initialize;

    protected void initialize() {
        state = initialized;
    }

    private PresenterState state;

    @Override
    public Presentable init() {
        this.state = uninitialized;
        prepare();
        return this;
    }

    @Override
    public ClientPresenter prepare() {
        state.process();
        return this;
    }

    protected String getConcrete() {
        return this.getClass().getCanonicalName();
    }

    protected <E extends DominoEvent> void fireEvent(Class<E> extensionPointInterface, E extensionPoint) {
        DominoEvents.fire(extensionPointInterface, extensionPoint);
    }

    protected void runAsync(AsyncRunner.AsyncTask asyncTask) {
        ClientApp.make().getAsyncRunner().runAsync(asyncTask);
    }

    protected String getName() {
        return ClientApp.make().getPresentersRepository().getNameFromConcreteName(getConcrete());
    }

    protected DominoHistory history() {
        return ClientApp.make().getHistory();
    }
}
