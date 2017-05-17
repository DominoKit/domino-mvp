package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.server.config.ServerConfiguration;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import io.vertx.ext.web.RoutingContext;

public class TestEntryPointContext extends VertxEntryPointContext {

    public TestEntryPointContext(RoutingContext routingContext, ServerConfiguration serverConfiguration) {
        super(routingContext, serverConfiguration);
    }
}
