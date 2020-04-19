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
public class WebJarResourcesConfiguratorPlugin extends BaseDominoLoaderPlugin {

    @Override
    public void applyPlugin(CompleteHandler completeHandler) {
        context.getRouter().route("/assets/lib/*")
                .order(Integer.MAX_VALUE - 3)
                .handler(StaticHandler.create("META-INF/resources/webjars"));
        completeHandler.onCompleted();
    }

    @Override
    public int order() {
        return PluginContext.WEBJARS_RESOURCES_HOLDER;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
