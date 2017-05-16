package com.progressoft.brix.domino.apt.server.multiHandlers;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.api.server.VertxEntryPointContext;

public class FirstHandlerEndpointHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        ServerApp serverApp=ServerApp.make();
        FirstRequest requestBody = Json.decodeValue(routingContext.getBodyAsString(), FirstRequest.class);
        ServerResponse response = (ServerResponse) serverApp
                .executeRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config()));
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(Json.encode(response));
    }
}