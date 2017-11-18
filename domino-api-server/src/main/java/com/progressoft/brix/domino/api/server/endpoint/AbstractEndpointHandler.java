package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractEndpointHandler<R extends RequestBean, S extends ResponseBean> extends AbstractEndpoint<R, S> {

    protected void executeRequest(RoutingContext routingContext, ServerApp serverApp, RequestContext<R> requestContext) {
        S response = (S) serverApp.executeRequest(requestContext, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(), routingContext.vertx()));
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(Json.encode(response));
    }

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
