package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.context.DefaultExecutionContext;
import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.request.DefaultMultiValuesMap;
import com.progressoft.brix.domino.api.server.request.DefaultRequestContext;
import com.progressoft.brix.domino.api.server.request.MultiValuesMap;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.server.response.ResponseContext;
import com.progressoft.brix.domino.api.server.response.VertxResponseContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public abstract class AbstractEndpoint<R extends RequestBean, S extends ResponseBean> implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext routingContext) {
        try {
            R requestBody = fetchBody(routingContext, routingContext.request().method());
            RequestContext<R> requestContext = DefaultRequestContext.forRequest(requestBody)
                    .requestKey(extractRequestKey(routingContext, requestBody))
                    .headers(getHeaders(routingContext))
                    .parameters(getParameters(routingContext)).build();
            ResponseContext<S> responseContext = new VertxResponseContext<>(routingContext);
            executeRequest(routingContext, ServerApp.make(), new DefaultExecutionContext<>(requestContext, responseContext));
        } catch (Exception e) {
            routingContext.fail(e);
        }
    }

    private String extractRequestKey(RoutingContext routingContext, R requestBody) {
        String requestKey = routingContext.request().getHeader("REQUEST_KEY");
        if (nonNull(requestKey) && !requestKey.isEmpty())
            return requestKey;

        return requestBody.getClass().getCanonicalName();
    }

    private MultiValuesMap<String, String> getParameters(RoutingContext routingContext) {
        return asMultiValuesMap(routingContext.request().params());
    }

    private MultiValuesMap<String, String> getHeaders(RoutingContext routingContext) {
        return asMultiValuesMap(routingContext.request().headers());
    }

    private MultiValuesMap<String, String> asMultiValuesMap(MultiMap multiMap) {
        MultiValuesMap<String, String> multiValuesMap = new DefaultMultiValuesMap<>();
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
