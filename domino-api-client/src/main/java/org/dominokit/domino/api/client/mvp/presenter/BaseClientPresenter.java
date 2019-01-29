package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.async.AsyncRunner;
import org.dominokit.domino.api.client.extension.DominoEvents;
import org.dominokit.domino.api.shared.extension.ActivationEventContext;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.history.AppHistory;
import org.dominokit.domino.api.shared.history.DominoHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseClientPresenter extends ClientPresenter implements Presentable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseClientPresenter.class);
    private PresenterState state;
    protected boolean activated;

    private final PresenterState initialized = () ->
            LOGGER.info("Presenter " + BaseClientPresenter.this.getClass() + " Have already been initialized.");

    private final PresenterState uninitialized = this::initialize;

    private Map<Class<? extends DominoEvent>, DominoEventListener> listeners;

    protected void initialize() {
        this.listeners = getListeners();
        registerListeners();
        state = initialized;
        activate();
        if(isAutoActivate()){
            activate();
        }

    }

    protected void activate() {
        fireStateEvent(true);
        onActivated();
    }

    protected void fireStateEvent(boolean state){
        fireActivationEvent(new ActivationEventContext(state));
    }

    protected void fireActivationEvent(ActivationEventContext context){
    }

    private void registerListeners(){
        listeners.forEach((key, value) -> ClientApp.make().registerEventListener(key, value));
    }

    private void removeListeners(){
        listeners.forEach((key, value) -> ClientApp.make().removeEventListener(key, value));
    }

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

    protected final void deActivate(){
        removeListeners();
        fireStateEvent(false);
        onDeactivated();
    }

    @Override
    protected void onActivated() {

    }

    @Override
    protected void onDeactivated() {

    }

    public void setState(DominoHistory.State state){

    }

    protected boolean isAutoActivate(){
        return true;
    }

    public final boolean isActivated() {
        return activated;
    }

    protected Map<Class<? extends DominoEvent>, DominoEventListener> getListeners() {
        return new HashMap<>();
    }

    protected <E extends DominoEvent> void fireEvent(Class<E> extensionPointInterface, E extensionPoint) {
        DominoEvents.fire(extensionPointInterface, extensionPoint);
    }

    protected void runAsync(AsyncRunner.AsyncTask asyncTask) {
        ClientApp.make().getAsyncRunner().runAsync(asyncTask);
    }

    protected AppHistory history() {
        return ClientApp.make().getHistory();
    }
}
