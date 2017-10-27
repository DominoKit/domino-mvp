package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

@Handler("somePath2")
public class SecondHandler implements RequestHandler<SecondRequest, ResponseBean>{
    @Override
    public ResponseBean handleRequest(SecondRequest request) {
        return null;
    }

}
