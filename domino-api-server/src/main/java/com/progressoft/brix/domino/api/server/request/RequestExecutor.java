package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public interface RequestExecutor {
    ResponseBean executeRequest(RequestContext requestContext, ServerEntryPointContext context);

    void executeCallbackRequest(RequestContext requestContext, ServerEntryPointContext context, CallbackRequestHandler.ResponseCallback<ResponseBean> responseCallback);
}
