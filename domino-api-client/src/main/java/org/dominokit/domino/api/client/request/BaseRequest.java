package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;

public abstract class BaseRequest implements Request {

    public static final String REQUEST_HAVE_ALREADY_BEEN_SENT = "Request have already been sent";

    protected final String key;
    protected RequestState state;
    protected final ClientApp clientApp=ClientApp.make();

    protected final RequestState<DefaultRequestStateContext> ready= context -> startRouting();

    protected final RequestState<DefaultRequestStateContext> completed= context -> {
        throw new InvalidRequestState("This request have already been completed!.");
    };

    public BaseRequest() {
        this.key=this.getClass().getCanonicalName();
        this.state=ready;
    }

    @Override
    public String getKey() {
        return this.key;
    }


    protected void execute() {
        if(!state.equals(ready))
            throw new InvalidRequestState(REQUEST_HAVE_ALREADY_BEEN_SENT);
        this.state.execute(new DefaultRequestStateContext());
    }

    protected Presentable getRequestPresenter() {
        return clientApp.getPresentersRepository().getPresenter(getPresenterName());
    }

    private String getPresenterName() {
        return clientApp.getRequestRepository().findRequestPresenterWrapper(this.getKey()).getPresenterName();
    }

    @Override
    public void applyState(RequestStateContext context) {
        state.execute(context);
    }

}
