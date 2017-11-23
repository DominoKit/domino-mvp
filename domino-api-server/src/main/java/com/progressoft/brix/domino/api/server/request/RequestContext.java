package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.shared.request.RequestBean;

public interface RequestContext<T extends RequestBean> {
    T getRequestBean();

    MultiMap<String, String> headers();

    MultiMap<String, String> parameters();

    String getRequestPath();
}
