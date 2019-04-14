package org.dominokit.domino.api.client;

import org.dominokit.domino.api.client.request.RequestInterceptor;

public interface CanSetDominoOptions {
    CanSetDominoOptions setDefaultServiceRoot(String defaultServiceRoot);

    CanSetDominoOptions setDefaultJsonDateFormat(String defaultJsonDateFormat);

    CanSetDominoOptions addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot);

    CanSetDominoOptions addRequestInterceptor(RequestInterceptor interceptor);
    CanSetDominoOptions removeRequestInterceptor(RequestInterceptor interceptor);

    CanSetDominoOptions setApplicationStartHandler(ApplicationStartHandler applicationStartHandler);

    CanSetDominoOptions setDefaultResourceRootPath(String rootPath);

}
