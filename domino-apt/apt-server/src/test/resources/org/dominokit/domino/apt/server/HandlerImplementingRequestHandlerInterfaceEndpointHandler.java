package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.endpoint.AbstractEndpoint;
import org.dominokit.domino.api.shared.request.ResponseBean;

public class HandlerImplementingRequestHandlerInterfaceEndpointHandler extends AbstractEndpoint<FirstRequest, ResponseBean> {

    @Override
    protected FirstRequest makeNewRequest() {
        return new FirstRequest();
    }

    @Override
    protected Class<FirstRequest> getRequestClass() {
        return FirstRequest.class;
    }
}