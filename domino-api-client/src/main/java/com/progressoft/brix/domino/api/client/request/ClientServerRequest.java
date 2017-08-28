package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ClientServerRequest<P extends Presentable, R extends ServerRequest, S extends ServerResponse>
        extends BaseRequest implements ServerFailedHandler<P, R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServerRequest.class);

    private Map<String, String> headers = new HashMap<>();

    private R serverArgs;

    private final RequestState<ServerResponseRecievedStateContext> sent;

    private final RequestState<ServerSuccessRequestStateContext> executedOnServer = context -> {
        process((P) getRequestPresenter(), serverArgs, (S) context.serverResponse);
        state = completed;
        if (Objects.nonNull(chainedRequest))
            chainedRequest.send();
    };

    private final RequestState<ServerFailedRequestStateContext> failedOnServer =
            new RequestState<ServerFailedRequestStateContext>() {
                @Override
                public void execute(ServerFailedRequestStateContext context) {
                    onFailedServerCall((P) getRequestPresenter(), serverArgs, context.response);
                    state = completed;
                }

                private void onFailedServerCall(P presenter, R serverArgs, FailedServerResponse failedResponse) {
                    LOGGER.debug("Could not execute request on server!.", failedResponse.getError());
                    processFailed(presenter, serverArgs, failedResponse);
                }
            };

    protected ClientServerRequest() {
        sent = context -> {
            if (context.nextContext instanceof ServerSuccessRequestStateContext) {
                state = executedOnServer;
                applyState(context.nextContext);
            } else if (context.nextContext instanceof ServerFailedRequestStateContext) {
                state = failedOnServer;
                applyState(context.nextContext);
            } else {
                throw new InvalidRequestState(
                        "Request cannot be processed until a serverResponse is recieved from the server");
            }
        };
    }

    @Override
    public void startRouting() {
        state = sent;
        clientApp.getServerRouter().routeRequest(this);
    }

    protected abstract void process(P presenter, R serverArgs, S response);

    public abstract R buildArguments();

    public R arguments() {
        this.serverArgs = buildArguments();
        this.serverArgs.setRequestKey(getKey());
        return this.serverArgs;
    }

    protected ClientServerRequest<P, R, S> setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> headers() {
        return new HashMap<>(headers);
    }
}
