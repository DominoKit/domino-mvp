package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.rest.DominoRestConfig;

@AutoService(DominoLoaderPlugin.class)
public class RequestContextPlugin extends BaseDominoLoaderPlugin {
    @Override
    protected void applyPlugin(CompleteHandler completeHandler) {
        DominoRestConfig.initDefaults();
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
