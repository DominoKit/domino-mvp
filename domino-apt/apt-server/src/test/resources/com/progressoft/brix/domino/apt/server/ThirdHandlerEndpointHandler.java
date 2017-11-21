package com.progressoft.brix.domino.apt.server;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpoint;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class ThirdHandlerEndpointHandler extends AbstractEndpoint<ThirdRequest, ResponseBean> {

    @Override
    protected ThirdRequest makeNewRequest() {
        return new ThirdRequest();
    }

    @Override
    protected Class<ThirdRequest> getRequestClass() {
        return ThirdRequest.class;
    }
}