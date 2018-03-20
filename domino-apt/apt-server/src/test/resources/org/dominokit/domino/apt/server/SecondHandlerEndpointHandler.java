package org.dominokit.domino.apt.server;

import org.dominokit.domino.api.server.endpoint.AbstractEndpoint;
import org.dominokit.domino.api.shared.request.ResponseBean;

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