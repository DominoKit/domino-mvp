package com.progressoft.brix.domino.apt.server.callbackHandler;

import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;

public class HandlerImplementingRequestHandlerInterfaceEndpointHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        try {
            ServerApp serverApp = ServerApp.make();
            ServerRequest requestBody = Json.decodeValue(routingContext.getBodyAsString(), ServerRequest.class);
            serverApp.executeCallbackRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(),
                    routingContext.vertx()), new CallbackRequestHandler.ResponseCallback() {
                @Override
                public void complete(Object response) {
                    routingContext.response()
                            .putHeader("content-type", "application/json")
                            .end(Json.encode((ServerResponse)response));
                }

                @Override
                public void redirect(String location) {

                }
            });
        } catch (Exception e){
            routingContext.fail(e);
        }
    }
}