package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;
import org.dominokit.domino.api.server.config.ServerConfigurationLoader;

import static org.dominokit.domino.api.server.PluginContext.MODULES_LOADER_ORDER;

@AutoService(DominoLoaderPlugin.class)
public class ModulesConfigratorPlugin implements DominoLoaderPlugin {

    private PluginContext context;

    @Override
    public DominoLoaderPlugin init(PluginContext context) {
        this.context = context;
        return this;
    }

    @Override
    public void apply() {
        new ServerConfigurationLoader(context.getVertxContext())
                .loadModules();
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
