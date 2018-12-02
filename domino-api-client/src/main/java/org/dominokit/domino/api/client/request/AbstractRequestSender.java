package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.api.shared.request.VoidResponse;
import org.dominokit.jacksonapt.AbstractObjectMapper;
import org.dominokit.rest.client.FailedResponse;
import org.dominokit.rest.shared.Response;
import org.dominokit.rest.shared.RestfulRequest;

import java.util.Arrays;

import static java.util.Objects.isNull;

public abstract class AbstractRequestSender<R extends RequestBean, S extends ResponseBean> implements RequestRestSender<R, S> {

    @Override
    public void send(ServerRequest<R, S> request, ServerRequestCallBack callBack) {
        new RequestPathHandler<>(request, getPath(), getCustomRoot()).process(serverRequest -> serverRequest.setUrl(replaceRequestParameters(serverRequest.getUrl(), serverRequest.requestBean())));
        RestfulRequest restfulRequest = RestfulRequest.get(request.getUrl())
                .putHeaders(request.headers())
                .putParameters(request.parameters())
                .onSuccess(response -> {
                            if (Arrays.stream(getSuccessCodes()).anyMatch(code -> code.equals(response.getStatusCode()))) {
                                if (isNull(getResponseMapper())) {
                                    readResponse(callBack, response);
                                } else {
                                    callBack.onSuccess(new VoidResponse());
                                }
                            } else {
                                callBack.onFailure(new FailedResponseBean(response.getStatusCode(), response.getStatusText(), response.getBodyAsString(), response.getHeaders()));
                            }
                        }
                ).onError(throwable -> callBack.onFailure(new FailedResponseBean(throwable)));
        if ("get".equalsIgnoreCase(getMethod())) {
            restfulRequest.send();
        } else {
            restfulRequest.send(getRequestMapper().write(request.requestBean()));
        }
    }

    protected abstract void readResponse(ServerRequestCallBack callBack, Response response);

    protected abstract String getPath();

    protected abstract Integer[] getSuccessCodes();

    protected abstract String replaceRequestParameters(String path, R request);

    protected abstract AbstractObjectMapper<R> getRequestMapper();

    protected abstract AbstractObjectMapper<S> getResponseMapper();

    protected abstract String getMethod();

    protected abstract String getCustomRoot();

}

