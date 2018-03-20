package org.dominokit.domino.api.server.request;

import org.dominokit.domino.api.shared.request.RequestBean;

public class DefaultRequestContext<T extends RequestBean> implements RequestContext<T> {

    private final T requestBean;
    private final MultiMap<String, String> parameters;
    private final MultiMap<String, String> headers;
    private final String requestPath;

    public DefaultRequestContext(String requestPath, T requestBean, MultiMap<String, String> parameters, MultiMap<String, String> headers) {
        this.requestPath = requestPath;
        this.requestBean = requestBean;
        this.parameters = parameters;
        this.headers = headers;
    }

    @Override
    public T getRequestBean() {
        return requestBean;
    }

    @Override
    public MultiMap<String, String> headers() {
        return headers;
    }

    @Override
    public MultiMap<String, String> parameters() {
        return parameters;
    }

    @Override
    public String getRequestPath() {
        return requestPath;
    }

    public static <S extends RequestBean> RequestContextBuilder<S> forRequest(S requestBean) {
        return new RequestContextBuilder<>(requestBean);
    }

    public static class RequestContextBuilder<S extends RequestBean> {

        private String requestPath;
        private S requestBean;
        private MultiMap<String, String> parameters;
        private MultiMap<String, String> headers;

        public RequestContextBuilder(S requestBean) {
            this.requestBean = requestBean;
        }

        public RequestContextBuilder<S> requestPath(String requestPath) {
            this.requestPath = requestPath;
            return this;
        }

        public RequestContextBuilder<S> parameters(MultiMap<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public RequestContextBuilder<S> headers(MultiMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public RequestContext<S> build() {
            return new DefaultRequestContext<>(requestPath, requestBean, parameters, headers);
        }
    }
}
