package org.dominokit.domino.api.server;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.CSRFHandlerImpl;

import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class DominoCSRFHandler extends CSRFHandlerImpl {
    private final JsonObject config;
    private Set<CSRFWhiteListHandler> whiteListHandlers;

    DominoCSRFHandler(String secret, JsonObject config) {
        super(secret);
        this.config = config;
        this.whiteListHandlers=new LinkedHashSet<>();
        ServiceLoader.load(CSRFWhiteListHandler.class).forEach(h -> whiteListHandlers.add(h));
    }

    @Override
    public void handle(RoutingContext ctx) {

        HttpMethod method = ctx.request().method();

        switch (method) {

            case POST:
            case PUT:
            case DELETE:
            case PATCH:
                final Set<CSRFWhiteListHandler> blackList = whiteListHandlers.stream()
                        .filter(w -> w.match(ctx, config) && !w.whiteList(ctx, config))
                        .collect(Collectors.toSet());
                if (blackList.isEmpty())
                    ctx.next();
                else
                    super.handle(ctx);
                break;
            default:
                super.handle(ctx);
                break;
        }
    }
}
