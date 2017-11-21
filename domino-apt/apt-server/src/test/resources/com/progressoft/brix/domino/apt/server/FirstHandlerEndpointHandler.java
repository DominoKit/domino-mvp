package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpoint;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class FirstHandlerEndpointHandler extends AbstractEndpoint<FirstRequest, ResponseBean> {

    @Override
    protected FirstRequest makeNewRequest() {
        return new FirstRequest();
    }

    @Override
    protected Class<FirstRequest> getRequestClass() {
        return FirstRequest.class;
    }
}