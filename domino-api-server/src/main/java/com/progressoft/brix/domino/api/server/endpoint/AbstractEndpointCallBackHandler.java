package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractEndpointCallBackHandler<R extends ServerRequest, S extends ServerResponse> extends AbstractEndpoint<R,S> {

    protected void executeRequest(RoutingContext routingContext, ServerApp serverApp, R requestBody) {
        serverApp.executeCallbackRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(),
                routingContext.vertx()), new CallbackRequestHandler.ResponseCallback<S>() {
            @Override
            public void complete(S response) {
                routingContext.response()
                        .putHeader("content-type", "application/json")
                        .end(Json.encode((ServerResponse)response));
            }

            @Override
            public void redirect(String location) {
                routingContext.response().setStatusCode(302).putHeader("Location",location).end();
            }
        });
    }

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
