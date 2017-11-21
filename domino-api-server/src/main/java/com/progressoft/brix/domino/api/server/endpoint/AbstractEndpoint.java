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

import static java.util.Objects.nonNull;

public abstract class AbstractEndpoint<R extends RequestBean, S extends ResponseBean> implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext routingContext) {
        try {
            R requestBody = fetchBody(routingContext, routingContext.request().method());
            processRequestKey(routingContext, requestBody);
            RequestContext<R> requestContext = new DefaultRequestContext<>(requestBody, getParameters(routingContext), getHeaders(routingContext));
            ResponseContext<S> responseContext = new VertxResponseContext<>(routingContext);
            executeRequest(routingContext, ServerApp.make(), new DefaultExecutionContext<>(requestContext, responseContext));
        } catch (Exception e) {
            routingContext.fail(e);
        }
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
            requestBody = Json.decodeValue(routingContext.getBodyAsString(), getRequestClass());
        } else {
            requestBody = makeNewRequest();
        }
        return requestBody;
    }

    private void processRequestKey(RoutingContext routingContext, R requestBody) {
        String requestKey = routingContext.request().getHeader("REQUEST_KEY");
        if (nonNull(requestKey) && !requestKey.isEmpty()) {
            requestBody.setRequestKey(requestKey);
        } else {
            requestBody.setRequestKey(requestBody.getClass().getCanonicalName());
        }
    }

    private void executeRequest(RoutingContext routingContext, ServerApp serverApp, ExecutionContext<R, S> requestContext) {
        serverApp.executeRequest(requestContext, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(), routingContext.vertx()));
    }

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
