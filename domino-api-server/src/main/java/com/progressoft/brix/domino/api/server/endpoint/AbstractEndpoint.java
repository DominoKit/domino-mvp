package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.context.DefaultExecutionContext;
import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.request.DefaultMultiMap;
import com.progressoft.brix.domino.api.server.request.DefaultRequestContext;
import com.progressoft.brix.domino.api.server.request.MultiMap;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.server.response.ResponseContext;
import com.progressoft.brix.domino.api.server.response.VertxResponseContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static java.util.Objects.isNull;

public abstract class AbstractEndpoint<R extends RequestBean, S extends ResponseBean> implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext routingContext) {
        try {
            R requestBody = fetchBody(routingContext, routingContext.request().method());
            RequestContext<R> requestContext = DefaultRequestContext.forRequest(requestBody)
                    .requestPath(routingContext.request().path())
                    .headers(getHeaders(routingContext))
                    .parameters(getParameters(routingContext)).build();
            ResponseContext<S> responseContext = new VertxResponseContext<>(routingContext);
            executeRequest(routingContext, ServerApp.make(), new DefaultExecutionContext<>(requestContext, responseContext));
        } catch (Exception e) {
            routingContext.fail(e);
        }
    }

    private MultiMap<String, String> getParameters(RoutingContext routingContext) {
        return asMultiValuesMap(routingContext.request().params());
    }

    private MultiMap<String, String> getHeaders(RoutingContext routingContext) {
        return asMultiValuesMap(routingContext.request().headers());
    }

    private MultiMap<String, String> asMultiValuesMap(io.vertx.core.MultiMap multiMap) {
        MultiMap<String, String> multiValuesMap = new DefaultMultiMap<>();
        multiMap.entries().forEach(entry -> multiValuesMap.add(entry.getKey(), entry.getValue()));
        return multiValuesMap;
    }

    private R fetchBody(RoutingContext routingContext, HttpMethod method) {
        R requestBody;
        if (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method)) {
            requestBody = getRequestBody(routingContext);
        } else {
            requestBody = makeNewRequest();
        }
        return requestBody;
    }

    private R getRequestBody(RoutingContext routingContext) {
        String bodyAsString = routingContext.getBodyAsString();
        if (isNull(bodyAsString) || bodyAsString.trim().isEmpty())
            return makeNewRequest();
        return Json.decodeValue(bodyAsString, getRequestClass());
    }

    private void executeRequest(RoutingContext routingContext, ServerApp serverApp, ExecutionContext<R, S> requestContext) {
        serverApp.executeRequest(requestContext, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(), routingContext.vertx()));
    }

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
