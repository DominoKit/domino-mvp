package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.server.DominoLoader;
import com.progressoft.brix.domino.api.server.RouterConfigurator;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

import static java.util.Objects.nonNull;

public class DominoTestServer {

    private Router router;
    private JsonObject config;
    private Vertx vertx;
    private AfterLoadHandler afterHandler;
    private BeforeLoadHandler beforeHandler;
    private WebClient webClient;

    private DominoTestServer(Vertx vertx) {
        this.vertx = vertx;
        webClient = WebClient.create(vertx);
    }

    public void start(TestContext context) {
        config = new JsonObject().put("http.port", 0);
        RouterConfigurator routerConfigurator = new RouterConfigurator(vertx, config);
        router = routerConfigurator.configuredRouter();
        beforeLoad();
        new DominoLoader(vertx, router, config).start(context.asyncAssertSuccess(this::afterLoad));
    }

    private void beforeLoad() {
        if (nonNull(beforeHandler))
            beforeHandler.handle(new DominoTestContext(router, config));
    }

    private void afterLoad(HttpServer server) {
        if (nonNull(afterHandler))
            afterHandler.handle(new DominoTestContext(router, config), new HttpServerContext(server.actualPort(), webClient));
    }

    public DominoTestServer onAfterLoad(AfterLoadHandler afterHandler) {
        this.afterHandler = afterHandler;
        return this;
    }

    public DominoTestServer onBeforeLoad(BeforeLoadHandler beforeHandler) {
        this.beforeHandler = beforeHandler;
        return this;
    }

    public static DominoTestServer vertx(Vertx vertx) {
        return new DominoTestServer(vertx);
    }

    @FunctionalInterface
    public interface BeforeLoadHandler {
        void handle(DominoTestContext context);
    }

    @FunctionalInterface
    public interface AfterLoadHandler {
        void handle(DominoTestContext context, HttpServerContext serverContext);
    }

    public final static class DominoTestContext {
        private final Router router;
        private final JsonObject config;

        public DominoTestContext(Router router, JsonObject config) {
            this.router = router;
            this.config = config;
        }

        public Router getRouter() {
            return router;
        }

        public JsonObject getConfig() {
            return config;
        }
    }

    public final static class HttpServerContext {
        private final int actualPort;
        private final WebClient webClient;

        public HttpServerContext(int actualPort, WebClient webClient) {
            this.actualPort = actualPort;
            this.webClient = webClient;
        }

        public int getActualPort() {
            return actualPort;
        }

        public HttpRequest<Buffer> makeRequest(String path) {
            return webClient.post(actualPort, "localhost", "/" + path);
        }

        public HttpRequest<Buffer> makeServiceRequest(String path) {
            return webClient.post(actualPort, "localhost", "/service/" + path);
        }
    }
}
