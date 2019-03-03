package org.dominokit.domino.test.api;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.CSRFHandler;
import io.vertx.reactivex.core.http.HttpServer;
import org.dominokit.domino.api.server.DominoLoader;
import org.dominokit.domino.api.server.RouterConfigurator;
import org.dominokit.domino.api.server.SecretKey;

import static java.util.Objects.nonNull;

public class DominoTestServer implements TestServerContext {

    private Router router;
    private JsonObject config;
    private Vertx vertx;
    private AfterLoadHandler afterHandler;
    private BeforeLoadHandler beforeHandler;
    private WebClient webClient;
    private String csrfToken;
    private HttpServer httpServer;

    private DominoTestServer(Vertx vertx) {
        this.vertx = vertx;
        webClient = WebClient.create(vertx);
    }

    public void start() {
        config = new JsonObject().put("http.port", 0);
        String secret = SecretKey.generate();
        csrfToken = new CSRFToken(secret).generate();
        RouterConfigurator routerConfigurator = new RouterConfigurator(vertx, config, secret);
        router = routerConfigurator.configuredRouter();
        beforeLoad();
        new DominoLoader(vertx, router, config).start(httpServer -> {
            this.httpServer = httpServer;
            afterLoad();
        });
    }

    private void beforeLoad() {
        if (nonNull(beforeHandler))
            beforeHandler.handle(this);
    }

    private void afterLoad() {
        if (nonNull(afterHandler))
            afterHandler.handle(this);
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

    @Override
    public int getActualPort() {
        return httpServer.actualPort();
    }

    @Override
    public String getCsrfToken() {
        return csrfToken;
    }

    @Override
    public HttpRequest<Buffer> makeRequest(String path) {
        return webClient.post("/" + path)
                .host("localhost")
                .port(getActualPort())
                .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
    }

    @Override
    public HttpRequest<Buffer> makeRequest(String path, HttpMethod method) {
        return webClient.request(method, "/" + path)
                .host("localhost")
                .port(getActualPort())
                .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
    }

    @Override
    public HttpRequest<Buffer> makeServiceRequest(String path) {
        return webClient.post("/service/" + path)
                .host("localhost")
                .port(getActualPort())
                .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
    }

    @Override
    public HttpRequest<Buffer> makeServiceRequest(String path, HttpMethod method) {
        return webClient.request(method, "/service/" + path)
                .host("localhost")
                .port(getActualPort())
                .putHeader(CSRFHandler.DEFAULT_HEADER_NAME, csrfToken);
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public JsonObject getConfig() {
        return config;
    }

    @FunctionalInterface
    public interface BeforeLoadHandler {
        void handle(TestRoutingContext context);
    }

    @FunctionalInterface
    public interface AfterLoadHandler {
        void handle(TestServerContext context);
    }
}
