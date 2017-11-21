package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.shared.request.RequestBean;

public class DefaultRequestContext<T extends RequestBean> implements RequestContext<T> {

    private final T requestBean;
    private final MultiValuesMap<String, String> parameters;
    private final MultiValuesMap<String, String> headers;

    public DefaultRequestContext(T requestBean, MultiValuesMap<String, String> parameters, MultiValuesMap<String, String> headers) {
        this.requestBean = requestBean;
        this.parameters = parameters;
        this.headers = headers;
    }

    @Override
    public T getRequestBean() {
        return requestBean;
    }

    @Override
    public MultiValuesMap<String, String> headers() {
        return headers;
    }

    @Override
    public MultiValuesMap<String, String> parameters() {
        return parameters;
    }
}
