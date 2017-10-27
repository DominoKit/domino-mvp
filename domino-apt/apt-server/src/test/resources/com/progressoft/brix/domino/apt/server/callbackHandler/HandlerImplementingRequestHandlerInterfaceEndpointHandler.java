package com.progressoft.brix.domino.apt.server.callbackHandler;

import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;

public class HandlerImplementingRequestHandlerInterfaceEndpointHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        ServerApp serverApp = ServerApp.make();
        RequestBean requestBody = Json.decodeValue(routingContext.getBodyAsString(), RequestBean.class);
        serverApp.executeCallbackRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(),
                routingContext.vertx()), new CallbackRequestHandler.ResponseCallback() {
            @Override
            public void complete(Object response) {
                routingContext.response()
                        .putHeader("content-type", "application/json")
                        .end(Json.encode((ResponseBean) response));
            }

            @Override
            public void redirect(String location) {
                routingContext.response().setStatusCode(302).putHeader("Location",location).end();
            }

        });

    }
}