package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.vertx.core.http.HttpServerResponse;
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
        StaticHandler staticHandler = StaticHandler.create();
        if (nonNull(System.getProperty("domino.webroot.location"))) {
            staticHandler.setAllowRootFileSystemAccess(true);
            staticHandler.setWebRoot(systemWebRoot());
        } else {
            staticHandler.setWebRoot(context.getWebRoot());
        }

        context.getRouter().route("/").order(Integer.MAX_VALUE - 3)
                .handler(this::serveIndexPage);

        context.getRouter().route("/static/*").order(Integer.MAX_VALUE - 2)
                .handler(staticHandler)
                .failureHandler(this::serveResource);

        context.getRouter().route("/*").order(Integer.MAX_VALUE)
                .handler(staticHandler)
                .handler(this::resourceNotFound)
                .failureHandler(this::serveIndexPage);

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

    private void serveResource(RoutingContext routingContext) {
        if (!routingContext.response().ended()) {
            routingContext.response()
                    .sendFile(context.getWebRoot() + routingContext.request().path().replace("/static", ""), event -> {
                        if (event.failed())
                            serveIndexPage(routingContext);
                    });
        }
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
