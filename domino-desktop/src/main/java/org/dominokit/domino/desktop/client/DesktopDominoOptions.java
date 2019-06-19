package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;

public class DesktopDominoOptions implements DominoOptions {

    private ApplicationStartHandler applicationStartHandler;

    @Override
    public void applyOptions() {
        //not implemented yet
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
