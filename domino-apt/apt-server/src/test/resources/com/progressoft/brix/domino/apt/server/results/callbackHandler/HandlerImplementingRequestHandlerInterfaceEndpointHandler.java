package com.progressoft.brix.domino.apt.server.callbackHandler;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointCallBackHandler;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import java.lang.Class;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.EndpointsProcessor")
public class HandlerImplementingRequestHandlerInterfaceEndpointHandler extends AbstractEndpointCallBackHandler<ServerRequest, ServerResponse> {
    @Override
    protected ServerRequest makeNewRequest() {
        return new ServerRequest();
    }

    @Override
    protected Class<ServerRequest> getRequestClass() {
        return ServerRequest.class;
    }
}