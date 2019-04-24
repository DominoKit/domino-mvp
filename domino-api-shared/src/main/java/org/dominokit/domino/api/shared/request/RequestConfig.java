package org.dominokit.domino.api.shared.request;

import java.util.List;

public interface RequestConfig {
    <R extends Request> RequestRouter<R> getClientRouter();
    RequestRouter<ServerRequest> getServerRouter();
    String getDefaultServiceRoot();

    String getDefaultJsonDateFormat();

    List<DynamicServiceRoot> getServiceRoots();

    List<RequestInterceptor> getRequestInterceptors();

    String getDefaultResourceRootPath();
}
