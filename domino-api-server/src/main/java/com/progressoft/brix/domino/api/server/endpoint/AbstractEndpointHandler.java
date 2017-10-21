package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractEndpointHandler<R extends ServerRequest, S extends ServerResponse> extends AbstractEndpoint<R,S> {

    protected void executeRequest(RoutingContext routingContext, ServerApp serverApp, R requestBody) {
        S response=(S)serverApp.executeRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(), routingContext.vertx()));
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(Json.encode(response));
    }

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
