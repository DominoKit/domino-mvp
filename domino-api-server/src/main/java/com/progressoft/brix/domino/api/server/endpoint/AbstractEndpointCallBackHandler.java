package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractEndpointCallBackHandler<R extends RequestBean, S extends ResponseBean> extends AbstractEndpoint<R, S> {

    public static final int REDIRECT_FOUND = 302;

    protected void executeRequest(RoutingContext routingContext, ServerApp serverApp, RequestContext<R> requestContext) {
        serverApp.executeCallbackRequest(requestContext, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(),
                routingContext.vertx()), new CallbackRequestHandler.ResponseCallback<S>() {
            @Override
            public void complete(S response) {
                routingContext.response()
                        .putHeader("content-type", "application/json")
                        .end(Json.encode((ResponseBean) response));
            }

            @Override
            public void redirect(String location) {
                routingContext.response().setStatusCode(REDIRECT_FOUND).putHeader("Location", location).end();
            }
        });
    }

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
