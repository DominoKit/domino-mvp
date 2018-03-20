package org.dominokit.domino.service.discovery.generation;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.Vertx;

@ProxyGen
public interface TestService {

    static TestService create(Vertx vertx) {
        return new TestServiceImpl();
    }

    static TestService createProxy(Vertx vertx, String address) {
        return new TestServiceVertxEBProxy(vertx, address);
    }
}
