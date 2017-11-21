package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.shared.request.RequestBean;

public class DefaultRequestContext<T extends RequestBean> implements RequestContext<T> {

    private final T requestBean;
    private final MultiValuesMap<String, String> parameters;
    private final MultiValuesMap<String, String> headers;
    private final String requestKey;

    public DefaultRequestContext(String requestKey, T requestBean, MultiValuesMap<String, String> parameters, MultiValuesMap<String, String> headers) {
        this.requestKey = requestKey;
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

    @Override
    public String getRequestKey() {
        return requestKey;
    }

    public static <S extends RequestBean> RequestContextBuilder<S> forRequest(S requestBean) {
        return new RequestContextBuilder<>(requestBean);
    }

    public static class RequestContextBuilder<S extends RequestBean> {

        private String requestKey;
        private S requestBean;
        private MultiValuesMap<String, String> parameters;
        private MultiValuesMap<String, String> headers;

        public RequestContextBuilder(S requestBean) {
            this.requestBean = requestBean;
        }

        public RequestContextBuilder<S> requestKey(String requestKey) {
            this.requestKey = requestKey;
            return this;
        }

        public RequestContextBuilder<S> parameters(MultiValuesMap<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public RequestContextBuilder<S> headers(MultiValuesMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public RequestContext<S> build() {
            return new DefaultRequestContext<>(requestKey, requestBean, parameters, headers);
        }
    }
}
