package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;

public abstract class PresenterCommand<P extends Presentable> extends BaseRequest {

    private PresenterHandler<P> handler=presenter -> {};

    private final RequestState<Request.DefaultRequestStateContext> sent =
            context -> {
                handler.onReady((P) getRequestPresenter());
                state = completed;
            };

    @Override
    public void startRouting() {
        state = sent;
        clientApp.getClientRouter().routeRequest(this);
    }

    public final void send(){
        execute();
    }

    public PresenterCommand<P> onPresenterReady(PresenterHandler<P> presenterHandler){
        this.handler=presenterHandler;
        return this;
    }


    @Override
    public String getKey() {
        return this.key;
    }

    public interface PresenterHandler<P>{
        void onReady(P presenter);
    }
}
