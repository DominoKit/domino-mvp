package org.dominokit.domino.api.client;

import org.dominokit.domino.api.shared.request.RequestConfig;

public interface HasDominoOptions extends RequestConfig {
    ApplicationStartHandler getApplicationStartHandler();
}
