package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class ClientServerRequest<R extends ServerRequest, S extends ServerResponse>
        extends BaseRequest{

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServerRequest.class);

    private Map<String, String> headers = new HashMap<>();

    private R serverArgs;

    private Success<S> success = response -> {
        throw new SuccessHandlerNotProvidedException();
    };
    private Fail fail =
            failedResponse -> LOGGER.debug("could not execute request on server: ", failedResponse.getError());

    private final RequestState<ServerSuccessRequestStateContext> executedOnServer = context -> {
        success.onSuccess((S) context.serverResponse);
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
            ClientServerRequest.this.applyState(context.nextContext);
        } else if (context.nextContext instanceof ServerFailedRequestStateContext) {
            state = failedOnServer;
            ClientServerRequest.this.applyState(context.nextContext);
        } else {
            throw new InvalidRequestState(
                    "Request cannot be processed until a serverResponse is recieved from the server");
        }
    };

    protected ClientServerRequest() {
    }

    public final void send(R serverArgs){
        this.serverArgs=serverArgs;
        execute();
    }

    @Override
    public void startRouting() {
        state = sent;
        clientApp.getServerRouter().routeRequest(this);
    }


    public R arguments() {
        return this.serverArgs;
    }

    protected ClientServerRequest<R, S> setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> headers() {
        return new HashMap<>(headers);
    }

    public ClientServerRequest<R,S> onSuccess(Success<S> success){
        this.success =success;
        return this;
    }

    public ClientServerRequest<R,S> onFailed(Fail fail){
        this.fail =fail;
        return this;
    }

    @FunctionalInterface
    public interface Success<S> {
        void onSuccess(S response);
    }

    @FunctionalInterface
    public interface Fail {
        void onFail(FailedServerResponse failedResponse);
    }

    public static class SuccessHandlerNotProvidedException extends RuntimeException {
    }
}
