package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static java.util.Objects.nonNull;

public abstract class AbstractEndpoint<R extends ServerRequest, S extends ServerResponse> implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext routingContext) {
        try {
            R requestBody = fetchBody(routingContext, routingContext.request().method());
            processRequestKey(routingContext, requestBody);
            executeRequest(routingContext, ServerApp.make(), requestBody);
        } catch(Exception e) {
            routingContext.fail(e);
        }
    }

    private R fetchBody(RoutingContext routingContext, HttpMethod method) {
        R requestBody;
        if(HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method)) {
            requestBody= Json.decodeValue(routingContext.getBodyAsString(), getRequestClass());
        } else {
            requestBody = makeNewRequest();
        }
        return requestBody;
    }

    private void processRequestKey(RoutingContext routingContext, R requestBody) {
        String requestKey=routingContext.request().getHeader("REQUEST_KEY");
        if(nonNull(requestKey) && !requestKey.isEmpty()) {
            requestBody.setRequestKey(requestKey);
        } else {
            requestBody.setRequestKey(requestBody.getClass().getCanonicalName());
        }
    }

    protected abstract void executeRequest(RoutingContext routingContext, ServerApp serverApp, R requestBody);

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
