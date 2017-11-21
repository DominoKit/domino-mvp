package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.shared.request.RequestBean;

public interface RequestContext<T extends RequestBean> {
    T getRequestBean();

    MultiValuesMap<String, String> headers();

    MultiValuesMap<String, String> parameters();
}
