package org.dominokit.domino.api.client;

public interface CanSetDominoOptions {
    CanSetDominoOptions setDefaultServiceRoot(String defaultServiceRoot);

    CanSetDominoOptions setDefaultJsonDateFormat(String defaultJsonDateFormat);

    CanSetDominoOptions addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot);
}
