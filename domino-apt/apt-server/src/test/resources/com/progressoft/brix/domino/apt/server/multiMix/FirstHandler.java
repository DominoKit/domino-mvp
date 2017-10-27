package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

@Handler(value = "somePath", classifier = "xyz")
public class FirstHandler implements RequestHandler<FirstRequest, ResponseBean>{
    @Override
    public ResponseBean handleRequest(FirstRequest request) {
        return null;
    }

}
