package org.dominokit.domino.api.server.response;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.dominokit.domino.rest.shared.request.ResponseBean;

import java.util.List;

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
        putHeader("Content-Type", "application/json");
        end(Json.encode(body));
    }

    @Override
    public void end(String body) {
        routingContext.response().end(body);
    }

    @Override
    public void end(S[] bodyArray) {
        putHeader("Content-Type", "application/json");
        end(Json.encode(bodyArray));
    }

    @Override
    public void end(List<S> bodyList) {
        putHeader("Content-Type", "application/json");
        end(Json.encode(bodyList));
    }

    @Override
    public void endHandler(ResponseEndHandler endHandler) {
        routingContext.response().endHandler(event -> endHandler.onResponseEnded());
    }
}
