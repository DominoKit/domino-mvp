package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

@Handler("somePath3")
public class ThirdHandler implements RequestHandler<ThirdRequest, ServerResponse>{
    @Override
    public ServerResponse handleRequest(ThirdRequest request) {
        return null;
    }

}
