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
        context.getRouter().route("/")
                .order(Integer.MAX_VALUE - 2)
                .handler(this::serveIndexPage);

        configureNoCacheStaticRoute();
        configureCacheStaticRoute();

        completeHandler.onCompleted();
    }

    private void configureCacheStaticRoute() {
        Route route = context.getRouter()
                .route("/*")
                .order(Integer.MAX_VALUE)
                .handler(StaticHandler.create()
                        .setWebRoot(context.getWebRoot()));
        if (nonNull(System.getProperty("domino.webroot.location"))) {
            StaticHandler webRootStaticHandler = StaticHandler.create();
            webRootStaticHandler.setAllowRootFileSystemAccess(true);
            webRootStaticHandler.setWebRoot(systemWebRoot());
            route.handler(webRootStaticHandler);
        }
        route
                .handler(this::resourceNotFound)
                .failureHandler(this::resourceNotFound);
    }

    private void configureNoCacheStaticRoute() {
        Route noCacheHandler = context.getRouter()
                .routeWithRegex("/.*nocache.*")
                .order(Integer.MAX_VALUE - 1)
                .handler(StaticHandler.create()
                        .setCachingEnabled(false)
                        .setWebRoot(context.getWebRoot()));
        if (nonNull(System.getProperty("domino.webroot.location"))) {
            StaticHandler webRootStaticHandler = StaticHandler.create();
            webRootStaticHandler.setAllowRootFileSystemAccess(true);
            webRootStaticHandler.setWebRoot(systemWebRoot());
            webRootStaticHandler.setCachingEnabled(false);
            noCacheHandler.handler(webRootStaticHandler);
        }

        noCacheHandler
                .handler(this::resourceNotFound)
                .failureHandler(this::resourceNotFound);
    }

    private HttpServerResponse resourceNotFound(RoutingContext routingContext) {
        return serveIndexPage(routingContext, 404);
    }

    private String systemWebRoot() {
        return Paths.get(System.getProperty("domino.webroot.location")).toAbsolutePath().toString();
    }

    private HttpServerResponse serveIndexPage(RoutingContext routingContext) {
        return serveIndexPage(routingContext, 200);
    }

    private HttpServerResponse serveIndexPage(RoutingContext routingContext, int statusCode) {
        if(context.getConfig().getBoolean("serve.index.page", true)) {
            return IndexPageContext.INSTANCE.getIndexPageProvider().serveIndexPage(context, routingContext, statusCode);
        }else{
            HttpServerResponse httpServerResponse = routingContext.response().setStatusCode(statusCode);
            httpServerResponse.end();
            return httpServerResponse;
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
