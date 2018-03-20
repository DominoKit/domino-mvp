package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class ServerRequest<R extends RequestBean, S extends ResponseBean>
        extends BaseRequest implements Response<S>, CanFailOrSend{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRequest.class);

    private Map<String, String> headers = new HashMap<>();

    private R requestBean;

    private Success<S> success = response -> {
    };
    private Fail fail =
            failedResponse -> LOGGER.debug("could not execute request on server: ", failedResponse.getError());

    private final RequestState<ServerSuccessRequestStateContext> executedOnServer = context -> {
        success.onSuccess((S) context.responseBean);
        state = completed;
    };

    private final RequestState<ServerFailedRequestStateContext> failedOnServer =
            context -> {
                fail.onFail(context.response);
                state = completed;
            };

    private final RequestState<ServerResponseReceivedStateContext> sent=context -> {
        if (context.nextContext instanceof ServerSuccessRequestStateContext) {
            state = executedOnServer;
            ServerRequest.this.applyState(context.nextContext);
        } else if (context.nextContext instanceof ServerFailedRequestStateContext) {
            state = failedOnServer;
            ServerRequest.this.applyState(context.nextContext);
        } else {
            throw new InvalidRequestState(
                    "Request cannot be processed until a responseBean is recieved from the server");
        }
    };

    protected ServerRequest() {
    }

    protected ServerRequest(R requestBean) {
        this.requestBean = requestBean;
    }

    @Override
    public final void send(){
        execute();
    }

    public ServerRequest<R, S> setBean(R requestBean){
        this.requestBean = requestBean;
        return this;
    }

    @Override
    public void startRouting() {
        state = sent;
        clientApp.getServerRouter().routeRequest(this);
    }


    public R requestBean() {
        return this.requestBean;
    }

    protected ServerRequest<R, S> setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> headers() {
        return new HashMap<>(headers);
    }

    @Override
    public CanFailOrSend onSuccess(Success<S> success) {
        this.success =success;
        return this;
    }

    @Override
    public CanSend onFailed(Fail fail) {
        this.fail=fail;
        return this;
    }
}
