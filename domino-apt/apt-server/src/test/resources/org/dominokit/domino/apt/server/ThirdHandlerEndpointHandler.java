package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.endpoint.AbstractEndpoint;
import org.dominokit.domino.api.shared.request.ResponseBean;

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