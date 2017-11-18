package com.progressoft.brix.domino.apt.server.multiHandlers;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointHandler;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class FirstHandlerEndpointHandler extends AbstractEndpointHandler<FirstRequest, ResponseBean> {

    @Override
    protected FirstRequest makeNewRequest() {
        return new FirstRequest();
    }

    @Override
    protected Class<FirstRequest> getRequestClass() {
        return FirstRequest.class;
    }
}