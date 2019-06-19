package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.reactivex.disposables.Disposable;
import io.vertx.core.AsyncResult;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.reactivex.core.http.HttpServer;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.ServerApp;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@AutoService(DominoLoaderPlugin.class)
public class ServerStartupPlugin extends BaseDominoLoaderPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerStartupPlugin.class);

    @Override
    protected void applyPlugin(CompleteHandler completeHandler) {
        startHttpServer(context.getOptions(), context.getHttpServerConsumer());
    }

    private Disposable startHttpServer(AsyncResult<HttpServerOptions> options, Consumer<HttpServer> httpServerConsumer) {
        return context.getRxVertx().createHttpServer(options.result())
                .requestHandler(context.getRxRouter())
                .rxListen(options.result().getPort())
                .doOnSuccess(httpServer -> {
                    LOGGER.info("Server started on port : " + httpServer.actualPort());
                    httpServerConsumer.accept(httpServer);
                })
                .doOnError(throwable -> LOGGER.error("Failed to start server", throwable))
                .subscribe();
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
