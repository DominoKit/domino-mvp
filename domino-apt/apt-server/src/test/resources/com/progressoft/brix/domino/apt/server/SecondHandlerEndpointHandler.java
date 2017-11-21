package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpoint;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class SecondHandlerEndpointHandler extends AbstractEndpoint<SecondRequest, ResponseBean> {

    @Override
    protected SecondRequest makeNewRequest() {
        return new SecondRequest();
    }

    @Override
    protected Class<SecondRequest> getRequestClass() {
        return SecondRequest.class;
    }
}