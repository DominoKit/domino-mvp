package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;

public class FakeDominoOptions implements DominoOptions {

    private ApplicationStartHandler applicationStartHandler;

    @Override
    public void applyOptions() {
        //no need to apply things now
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
