package org.dominokit.domino.api.server.plugins;

import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.PluginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

public abstract class BaseDominoLoaderPlugin implements DominoLoaderPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDominoLoaderPlugin.class);
    protected PluginContext context;
    protected DominoLoaderPlugin nextPlugin;

    @Override
    public DominoLoaderPlugin init(PluginContext context) {
        this.context = context;
        return this;
    }

    @Override
    public void setNext(DominoLoaderPlugin nextPlugin) {
        this.nextPlugin = nextPlugin;
    }

    @Override
    public final void apply() {
        LOGGER.info("Applying plugin : " + this.getClass().getCanonicalName());
        if (isEnabled()) {
            applyPlugin(this::applyNext);
        } else {
            applyNext();
        }
    }

    private void applyNext() {
        if (nonNull(nextPlugin)) {
            nextPlugin.apply();
        }
    }

    protected abstract void applyPlugin(CompleteHandler completeHandler);

    @FunctionalInterface
    public interface CompleteHandler {
        void onCompleted();
    }

}
