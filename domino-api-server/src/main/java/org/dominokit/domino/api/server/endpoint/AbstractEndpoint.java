package org.dominokit.domino.api.server.endpoint;

import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.context.DefaultExecutionContext;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.entrypoint.VertxEntryPointContext;
import org.dominokit.domino.api.server.request.DefaultMultiMap;
import org.dominokit.domino.api.server.request.DefaultRequestContext;
import org.dominokit.domino.api.server.request.MultiMap;
import org.dominokit.domino.api.server.request.RequestContext;
import org.dominokit.domino.api.server.response.ResponseContext;
import org.dominokit.domino.api.server.response.VertxResponseContext;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.context.DefaultExecutionContext;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.entrypoint.VertxEntryPointContext;
import org.dominokit.domino.api.server.request.DefaultMultiMap;
import org.dominokit.domino.api.server.request.DefaultRequestContext;
import org.dominokit.domino.api.server.request.MultiMap;
import org.dominokit.domino.api.server.request.RequestContext;
import org.dominokit.domino.api.server.response.ResponseContext;

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
