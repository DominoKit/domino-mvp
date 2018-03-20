package org.dominokit.domino.api.client;

import java.util.List;

public interface HasDominoOptions {
    String getDefaultServiceRoot();

    String getDefaultJsonDateFormat();

    List<DynamicServiceRoot> getServiceRoots();
}
