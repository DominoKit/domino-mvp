package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointHandler;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class ThirdHandlerEndpointHandler extends AbstractEndpointHandler<ThirdRequest, ResponseBean> {

    @Override
    protected ThirdRequest makeNewRequest() {
        return new ThirdRequest();
    }

    @Override
    protected Class<ThirdRequest> getRequestClass() {
        return ThirdRequest.class;
    }
}