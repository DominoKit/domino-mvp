package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.vertx.ext.web.handler.LoggerHandler;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;

@AutoService(DominoLoaderPlugin.class)
public class RequestsLoggingPlugin extends BaseDominoLoaderPlugin {

    @Override
    public void applyPlugin(CompleteHandler completeHandler) {
        context.getRouter().route("/*")
                .order(0)
                .handler(LoggerHandler.create());
        completeHandler.onCompleted();
    }

    @Override
    public int order() {
        return PluginContext.LOG_REQUESTS_ORDER;
    }

    @Override
    public boolean isEnabled() {
        return context.getConfig().getBoolean("http.request.logging.enabled", false);
    }
}
