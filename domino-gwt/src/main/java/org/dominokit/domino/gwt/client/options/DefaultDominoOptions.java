package org.dominokit.domino.gwt.client.options;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;

import static java.util.Objects.nonNull;

public class DefaultDominoOptions implements DominoOptions {
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
