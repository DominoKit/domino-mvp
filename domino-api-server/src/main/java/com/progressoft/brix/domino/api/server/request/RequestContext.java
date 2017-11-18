package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.shared.request.RequestBean;

import java.util.Map;

public class RequestContext<R extends RequestBean> {
    private final R requestBean;
    private final Map<String, String> metadata;

    public RequestContext(R requestBean, Map<String, String> metadata) {
        this.requestBean = requestBean;
        this.metadata = metadata;
    }

    public R getRequestBean() {
        return requestBean;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}
