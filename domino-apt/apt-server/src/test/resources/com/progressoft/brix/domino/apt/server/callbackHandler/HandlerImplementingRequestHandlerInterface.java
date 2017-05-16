package com.progressoft.brix.domino.apt.server.callbackHandler;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.api.server.Handler;
import com.progressoft.brix.domino.api.server.CallbackRequestHandler;

@Handler("somePath")
public class HandlerImplementingRequestHandlerInterface implements CallbackRequestHandler<ServerRequest, ServerResponse>{
    @Override
    public void handleRequest(ServerRequest request, ResponseCallback<ServerResponse> callback) {

    }

}
