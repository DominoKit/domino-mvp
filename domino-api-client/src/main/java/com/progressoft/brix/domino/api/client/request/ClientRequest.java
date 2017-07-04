package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;

import java.util.Objects;

public abstract class ClientRequest<P extends Presentable> extends BaseRequest {

    private final RequestState<Request.DefaultRequestStateContext> sent =
            context -> {
                process((P) getRequestPresenter());
                state = completed;
                if(Objects.nonNull(chainedRequest))
                    chainedRequest.send();
            };


    @Override
    public void startRouting() {
        state = sent;
        clientApp.getClientRouter().routeRequest(this);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    protected abstract void process(P presenter);

}
