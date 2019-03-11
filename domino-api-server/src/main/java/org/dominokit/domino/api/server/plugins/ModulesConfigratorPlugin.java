package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;
import org.dominokit.domino.api.server.config.ServerConfigurationLoader;

import static org.dominokit.domino.api.server.PluginContext.MODULES_LOADER_ORDER;

@AutoService(DominoLoaderPlugin.class)
public class ModulesConfigratorPlugin extends BaseDominoLoaderPlugin {

    @Override
    public void applyPlugin(CompleteHandler completeHandler) {
        new ServerConfigurationLoader(context.getVertxContext())
                .loadModules();
        completeHandler.onCompleted();
    }

    @Override
    public int order() {
        return MODULES_LOADER_ORDER;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
