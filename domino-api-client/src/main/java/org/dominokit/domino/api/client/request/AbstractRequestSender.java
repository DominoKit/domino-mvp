package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.history.HistoryToken;
import org.dominokit.domino.api.shared.history.StateHistoryToken;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.api.shared.request.VoidResponse;
import org.dominokit.jacksonapt.AbstractObjectMapper;
import org.dominokit.rest.shared.Response;
import org.dominokit.rest.shared.RestfulRequest;
import org.gwtproject.regexp.shared.MatchResult;
import org.gwtproject.regexp.shared.RegExp;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractRequestSender<R extends RequestBean, S extends ResponseBean> implements RequestRestSender<R, S> {

    private final List<String> SEND_BODY_METHODS = Arrays.asList("POST", "PUT", "PATCH");

    @Override
    public void send(ServerRequest<R, S> request, ServerRequestCallBack callBack) {
        new RequestPathHandler<>(request, getPath(), getCustomRoot()).process(serverRequest -> serverRequest.setUrl(formatUrl(serverRequest.getUrl(), serverRequest.requestBean())));
        ClientApp.make().dominoOptions().getRequestInterceptor()
                .interceptRequest(request, () -> {
                    RestfulRequest restfulRequest = RestfulRequest.request(request.getUrl(), getMethod().toUpperCase())
                            .putHeaders(request.headers())
                            .putParameters(request.parameters())
                            .onSuccess(response -> {
                                        if (Arrays.stream(getSuccessCodes()).anyMatch(code -> code.equals(response.getStatusCode()))) {
                                            if (isVoidResponse()) {
                                                callBack.onSuccess(new VoidResponse());
                                            } else {
                                                readResponse(callBack, response);
                                            }
                                        } else {
                                            callBack.onFailure(new FailedResponseBean(response.getStatusCode(), response.getStatusText(), response.getBodyAsString(), response.getHeaders()));
                                        }
                                    }
                            ).onError(throwable -> callBack.onFailure(new FailedResponseBean(throwable)));

                    if (SEND_BODY_METHODS.contains(getMethod().toUpperCase())) {
                        restfulRequest.sendJson(getRequestMapper().write(request.requestBean()));
                    } else {
                        restfulRequest.send();
                    }
                });
    }

    protected String formatUrl(String url, R requestBean) {
        String postfix = asTokenString(url);
        String prefix = url.replace(postfix, "");

        String formattedPostfix = replaceRequestParameters(new StateHistoryToken(postfix), requestBean);
        return prefix + formattedPostfix;
    }

    private String asTokenString(String url) {
        if (url.contains("http:") || url.contains("https:")) {
            RegExp regExp = RegExp.compile("^((.*:)//([a-z0-9\\-.]+)(|:[0-9]+)/)(.*)$");
            MatchResult matcher = regExp.exec(url);
            boolean matchFound = matcher != null; // equivalent to regExp.test(inputStr);

            if (matchFound) {
                String suffixPart = matcher.getGroup(matcher.getGroupCount() - 1);
                return suffixPart;
            }
        }

        return url;
    }

    protected abstract void readResponse(ServerRequestCallBack callBack, Response response);

    protected abstract String getPath();

    protected abstract Integer[] getSuccessCodes();

    protected abstract String replaceRequestParameters(HistoryToken token, R request);

    protected abstract AbstractObjectMapper<R> getRequestMapper();

    protected abstract String getMethod();

    protected abstract String getCustomRoot();

    protected abstract boolean isVoidResponse();

    protected abstract boolean isVoidRequest();

}

