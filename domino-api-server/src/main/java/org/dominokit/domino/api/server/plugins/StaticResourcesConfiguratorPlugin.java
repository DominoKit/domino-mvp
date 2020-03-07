package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;

import java.nio.file.Paths;

import static java.util.Objects.nonNull;

@AutoService(DominoLoaderPlugin.class)
public class StaticResourcesConfiguratorPlugin extends BaseDominoLoaderPlugin {

    @Override
    public void applyPlugin(CompleteHandler completeHandler) {
        context.getRouter().route("/").order(Integer.MAX_VALUE - 2)
                .handler(this::serveIndexPage);

        Route route = context.getRouter()
                .route("/*")
                .order(Integer.MAX_VALUE)
                .handler(StaticHandler.create().setWebRoot(context.getWebRoot()));
        if (nonNull(System.getProperty("domino.webroot.location"))) {
            StaticHandler webRootStaticHandler = StaticHandler.create();
            webRootStaticHandler.setAllowRootFileSystemAccess(true);
            webRootStaticHandler.setWebRoot(systemWebRoot());
            route.handler(webRootStaticHandler);
        }
        route
                .handler(this::resourceNotFound)
                .failureHandler(this::resourceNotFound);
        completeHandler.onCompleted();
    }

    private HttpServerResponse resourceNotFound(RoutingContext routingContext) {
        return routingContext.response()
                .putHeader("Content-type", "text/html")
                .setStatusCode(404)
                .sendFile(context.getWebRoot() + "/index.html");
    }

    private String systemWebRoot() {
        return Paths.get(System.getProperty("domino.webroot.location")).toAbsolutePath().toString();
    }

    private HttpServerResponse serveIndexPage(RoutingContext event) {
        return event.response().putHeader("Content-type", "text/html")
                .sendFile(context.getWebRoot() + "/index.html");
    }

    @Override
    public int order() {
        return PluginContext.STATIC_RESOURCES_ORDER;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
