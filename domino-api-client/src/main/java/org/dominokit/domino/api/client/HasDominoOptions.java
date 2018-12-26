package org.dominokit.domino.api.client;

import org.dominokit.domino.api.client.request.RequestInterceptor;

import java.util.List;

public interface HasDominoOptions {
    String getDefaultServiceRoot();

    String getDefaultJsonDateFormat();

    List<DynamicServiceRoot> getServiceRoots();

    RequestInterceptor getRequestInterceptor();

    ApplicationStartHandler getApplicationStartHandler();
}
