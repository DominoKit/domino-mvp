package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public interface RequestExecutor {
    ResponseBean executeRequest(RequestBean request, ServerEntryPointContext context);
    void executeCallbackRequest(RequestBean request, ServerEntryPointContext context, CallbackRequestHandler.ResponseCallback<ResponseBean> responseCallback);
}
