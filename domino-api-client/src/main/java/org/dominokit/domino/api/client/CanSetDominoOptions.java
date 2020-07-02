package org.dominokit.domino.api.client;


public interface CanSetDominoOptions {
    CanSetDominoOptions setApplicationStartHandler(ApplicationStartHandler applicationStartHandler);
    CanSetDominoOptions setMainApp(boolean mainApp);
}
