package com.progressoft.brix.domino.apt.server.callbackHandler;

import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;

@Handler(value = "somePath", classifier = "xyz")
public class HandlerImplementingRequestHandlerInterface implements CallbackRequestHandler<RequestBean, ResponseBean>{
    @Override
    public void handleRequest(RequestBean request, ResponseCallback<ResponseBean> callback) {
        //for generation testing only
    }

}
