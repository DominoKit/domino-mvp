package com.progressoft.brix.domino.api.server.endpoint;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public abstract class AbstractEndpoint<R extends RequestBean, S extends ResponseBean> implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext routingContext) {
        try {
            R requestBody = fetchBody(routingContext, routingContext.request().method());
            processRequestKey(routingContext, requestBody);
            RequestContext<R> requestContext = new RequestContext<>(requestBody, getMetaData(routingContext));
            executeRequest(routingContext, ServerApp.make(), requestContext);
        } catch (Exception e) {
            routingContext.fail(e);
        }
    }

    private Map<String, String> getMetaData(RoutingContext routingContext) {
        return Stream.of(routingContext.request().headers(), routingContext.request().params())
                .map(MultiMap::entries)
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (s, s2) -> String.join(s, s2, ",")));
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

    protected abstract void executeRequest(RoutingContext routingContext, ServerApp serverApp, RequestContext<R> requestContext);

    protected abstract R makeNewRequest();

    protected abstract Class<R> getRequestClass();
}
