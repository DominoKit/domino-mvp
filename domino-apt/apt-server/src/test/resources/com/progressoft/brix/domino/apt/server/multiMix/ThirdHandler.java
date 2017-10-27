package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

@Handler("somePath3")
public class ThirdHandler implements RequestHandler<ThirdRequest, ResponseBean>{
    @Override
    public ResponseBean handleRequest(ThirdRequest request) {
        return null;
    }

}
