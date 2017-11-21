package com.progressoft.brix.domino.api.server.context;

import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.server.response.ResponseContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public interface ExecutionContext<T extends RequestBean, S extends ResponseBean> {
    RequestContext<T> request();

    ResponseContext<S> response();
}
