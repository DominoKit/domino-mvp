package org.dominokit.domino.test.api;

import org.dominokit.domino.api.server.DominoLoader;
import org.dominokit.domino.api.server.RouterConfigurator;
import org.dominokit.domino.api.server.SecretKey;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.CSRFHandler;

import static java.util.Objects.nonNull;

public class DominoTestServer {

    private Router router;
    private JsonObject config;
    private Vertx vertx;
    private AfterLoadHandler afterHandler;
    private BeforeLoadHandler beforeHandler;
    private WebClient webClient;
    private String csrfToken;

    private DominoTestServer(Vertx vertx) {
        this.vertx = vertx;
        webClient = WebClient.create(vertx);
    }

    public void start(TestContext context) {
        config = new JsonObject().put("http.port", 0);
        String secret = SecretKey.generate();
        csrfToken = new CSRFToken(secret).generate();
        RouterConfigurator routerConfigurator = new RouterConfigurator(vertx, config, secret);
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
            afterHandler.handle(new DominoTestContext(router, config),
                    new HttpServerContext(server.actualPort(), webClient, csrfToken));
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

    public static final class DominoTestContext {
        private final Router router;
        private final JsonObject config;

        DominoTestContext(Router router, JsonObject config) {
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
        private final String csrfToken;

        public HttpServerContext(int actualPort, WebClient webClient, String csrfToken) {
            this.actualPort = actualPort;
            this.webClient = webClient;
            this.csrfToken = csrfToken;
        }

        public int getActualPort() {
            return actualPort;
        }

        public String getCsrfToken() {
            return csrfToken;
        }

        public HttpRequest<Buffer> makeRequest(String path) {
            return webClient.post( "/" + path)
                    .host("localhost")
                    .port(actualPort)
                    .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
        }

        public HttpRequest<Buffer> makeRequest(String path, HttpMethod method) {
            return webClient.request(method, "/" + path)
                    .host("localhost")
                    .port(actualPort)
                    .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
        }

        public HttpRequest<Buffer> makeServiceRequest(String path) {
            return webClient.post( "/service/" + path)
                    .host("localhost")
                    .port(actualPort)
                    .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
        }

        public HttpRequest<Buffer> makeServiceRequest(String path, HttpMethod method) {
            return webClient.request(method, "/service/" + path)
                    .host("localhost")
                    .port(actualPort)
                    .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
        }
    }
}
