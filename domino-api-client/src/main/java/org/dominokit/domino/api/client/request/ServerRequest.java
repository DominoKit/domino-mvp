package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

public abstract class ServerRequest<R extends RequestBean, S extends ResponseBean>
        extends BaseRequest implements Response<S>, CanFailOrSend, HasHeadersAndParameters<R, S> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRequest.class);

    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();

    private R requestBean;

    private String url;

    private Success<S> success = response -> {
    };
    private Fail fail =
            failedResponse -> {
                if (nonNull(failedResponse.getThrowable())) {
                    LOGGER.debug("could not execute request on server: ", failedResponse.getThrowable());
                }else{
                    LOGGER.debug("could not execute request on server: status ["+failedResponse.getStatusCode()+"], body ["+failedResponse.getBody()+"]");
                }
            };

    private final RequestState<ServerSuccessRequestStateContext> executedOnServer = context -> {
        success.onSuccess((S) context.responseBean);
        state = completed;
    };

    private final RequestState<ServerFailedRequestStateContext> failedOnServer =
            context -> {
                fail.onFail(context.response);
                state = completed;
            };

    private final RequestState<ServerResponseReceivedStateContext> sent = context -> {
        if (context.nextContext instanceof ServerSuccessRequestStateContext) {
            state = executedOnServer;
            ServerRequest.this.applyState(context.nextContext);
        } else if (context.nextContext instanceof ServerFailedRequestStateContext) {
            state = failedOnServer;
            ServerRequest.this.applyState(context.nextContext);
        } else {
            throw new InvalidRequestState(
                    "Request cannot be processed until a responseBean is received from the server");
        }
    };

    protected ServerRequest() {
    }

    protected ServerRequest(R requestBean) {
        this.requestBean = requestBean;
    }

    @Override
    public final void send() {
        execute();
    }

    public ServerRequest<R, S> setBean(R requestBean) {
        this.requestBean = requestBean;
        return this;
    }

    public ServerRequest<R, S> intercept(Consumer<HasHeadersAndParameters<R, S>> interceptor) {
        interceptor.accept(this);
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

    public ServerRequest<R, S> setHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public ServerRequest<R, S> setHeaders(Map<String, String> headers) {
        if (nonNull(headers) && !headers.isEmpty()) {
            this.headers.putAll(headers);
        }
        return this;
    }

    public ServerRequest<R, S> setParameter(String name, String value) {
        parameters.put(name, value);
        return this;
    }

    public ServerRequest<R, S> setParameters(Map<String, String> parameters) {
        if (nonNull(parameters) && !parameters.isEmpty()) {
            this.parameters.putAll(parameters);
        }
        return this;
    }

    public Map<String, String> headers() {
        return new HashMap<>(headers);
    }

    public Map<String, String> parameters() {
        return new HashMap<>(parameters);
    }

    public String getUrl() {
        return url;
    }

    public ServerRequest<R, S> setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public CanFailOrSend onSuccess(Success<S> success) {
        this.success = success;
        return this;
    }

    @Override
    public CanSend onFailed(Fail fail) {
        this.fail = fail;
        return this;
    }
}
