package com.progressoft.brix.domino.apt.server.singleHandler;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

@Handler("somePath")
public class HandlerImplementingRequestHandlerInterface implements RequestHandler<ServerRequest, ServerResponse>{
    @Override
    public ServerResponse handleRequest(ServerRequest arguments) {
        return null;
    }

}
