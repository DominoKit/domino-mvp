package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointHandler;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class SecondHandlerEndpointHandler extends AbstractEndpointHandler<SecondRequest, ResponseBean> {

    @Override
    protected SecondRequest makeNewRequest() {
        return new SecondRequest();
    }

    @Override
    protected Class<SecondRequest> getRequestClass() {
        return SecondRequest.class;
    }
}