package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.config.ServerRequestConfig;
import org.dominokit.domino.api.server.request.DefaultServerRouter;
import org.dominokit.domino.api.shared.request.RequestContext;

@AutoService(DominoLoaderPlugin.class)
public class RequestContextPlugin extends BaseDominoLoaderPlugin {
    @Override
    protected void applyPlugin(CompleteHandler completeHandler) {
        RequestContext.init(new ServerRequestConfig(context.getVertxContext(), new DefaultServerRouter()));
        completeHandler.onCompleted();
    }

    @Override
    public int order() {
        return 25;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
