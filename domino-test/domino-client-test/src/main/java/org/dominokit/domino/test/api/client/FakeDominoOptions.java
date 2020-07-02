package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;
import org.dominokit.domino.rest.DominoRestConfig;
import org.dominokit.domino.rest.shared.request.DynamicServiceRoot;

public class FakeDominoOptions implements DominoOptions {

    private ApplicationStartHandler applicationStartHandler;
    private boolean mainApp = false;

    @Override
    public void applyOptions() {
        //no need to apply things now
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

    @Override
    public CanSetDominoOptions setMainApp(boolean mainApp) {
        this.mainApp = mainApp;
        return this;
    }

    @Override
    public boolean isMainApp() {
        return mainApp;
    }
}
