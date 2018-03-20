package org.dominokit.domino.api.server.response;

import org.dominokit.domino.api.shared.request.ResponseBean;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class VertxResponseContext<S extends ResponseBean> implements ResponseContext<S> {

    private final RoutingContext routingContext;

    public VertxResponseContext(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    @Override
    public ResponseContext<S> putHeader(String name, String value) {
        routingContext.response().putHeader(name, value);
        return this;
    }

    @Override
    public ResponseContext<S> putHeader(String name, Iterable<String> values) {
        routingContext.response().putHeader(name, values);
        return this;
    }

    @Override
    public ResponseContext<S> statusCode(int statusCode) {
        routingContext.response().setStatusCode(statusCode);
        return this;
    }

    @Override
    public void end() {
        routingContext.response().end();
    }

    @Override
    public void end(S body) {
        end(Json.encode(body));
    }

    @Override
    public void end(String body) {
        routingContext.response().end(body);
    }
}
