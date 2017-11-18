package com.progressoft.brix.domino.apt.server.multiMix;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointHandler;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;

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