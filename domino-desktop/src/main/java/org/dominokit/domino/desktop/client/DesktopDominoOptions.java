package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;
import org.dominokit.domino.rest.DominoRestConfig;
import org.dominokit.domino.rest.shared.request.DynamicServiceRoot;

public class DesktopDominoOptions implements DominoOptions {

    private ApplicationStartHandler applicationStartHandler;

    @Override
    public void applyOptions() {
        //not implemented yet
    }

    @Override
    public DominoOptions addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot) {
        DominoRestConfig.getInstance().addDynamicServiceRoot(dynamicServiceRoot);
        return this;
    }

    @Override
    public CanSetDominoOptions setApplicationStartHandler(ApplicationStartHandler applicationStartHandler) {
        this.applicationStartHandler = applicationStartHandler;
        return this;
    }

    @Override
    public ApplicationStartHandler getApplicationStartHandler() {
        return applicationStartHandler;
    }

}
