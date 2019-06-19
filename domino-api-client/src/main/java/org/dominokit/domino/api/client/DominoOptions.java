package org.dominokit.domino.api.client;

import org.dominokit.domino.rest.shared.request.DynamicServiceRoot;

public interface DominoOptions extends HasDominoOptions, CanSetDominoOptions {
    void applyOptions();

    DominoOptions addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot);
}
