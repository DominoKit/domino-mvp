package com.progressoft.brix.domino.apt.server.multiHandlers;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointHandler;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.EndpointsProcessor")
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