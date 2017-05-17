package com.progressoft.brix.domino.apt.server.multiHandlers;

import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

@Handler("somePath")
public class FirstHandler implements RequestHandler<FirstRequest, ServerResponse>{
    @Override
    public ServerResponse handleRequest(FirstRequest request) {
        return null;
    }

}
