package org.dominokit.domino.apt.server;

import javax.annotation.Generated;
import org.dominokit.domino.api.server.endpoint.AbstractEndpoint;
import org.dominokit.domino.api.shared.request.ResponseBean;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.server.EndpointsProcessor")
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