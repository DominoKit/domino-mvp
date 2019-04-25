package org.dominokit.domino.api.server.config;

import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.dominokit.domino.api.shared.request.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ServerRequestConfig implements RequestConfig {

    private String defaultServiceRoot;
    private String defaultResourceRootPath = "service";
    private String defaultJsonDateFormat = null;
    private List<DynamicServiceRoot> dynamicServiceRoots = new ArrayList<>();
    private final List<RequestInterceptor> interceptors = new ArrayList<>();
    private VertxContext vertxContext;
    private RequestRouter<ServerRequest> serverRouter;

    public ServerRequestConfig(VertxContext vertxContext, RequestRouter<ServerRequest> serverRouter) {
        this.vertxContext = vertxContext;
        this.serverRouter = serverRouter;
    }

    @Override
    public List<RequestInterceptor> getRequestInterceptors() {
        return interceptors;
    }


    @Override
    public String getDefaultServiceRoot() {
        if(isNull(defaultServiceRoot)){
            return "http://"+vertxContext.httpServerOptions().getHost()+":"+vertxContext.httpServerOptions().getPort()+"/"+defaultResourceRootPath+"/";
        }
        return defaultServiceRoot;
    }

    @Override
    public String getDefaultJsonDateFormat() {
        return defaultJsonDateFormat;
    }

    @Override
    public List<DynamicServiceRoot> getServiceRoots() {
        return dynamicServiceRoots;
    }

    @Override
    public RequestRouter<ServerRequest> getClientRouter() {
        return serverRouter;
    }

    @Override
    public RequestRouter<ServerRequest> getServerRouter() {
        return serverRouter;
    }

    @Override
    public String getDefaultResourceRootPath() {
        return defaultResourceRootPath;
    }

    public void setDefaultServiceRoot(String defaultServiceRoot) {
        this.defaultServiceRoot = defaultServiceRoot;
    }

    public void setDefaultResourceRootPath(String defaultResourceRootPath) {
        this.defaultResourceRootPath = defaultResourceRootPath;
    }

    public void setDefaultJsonDateFormat(String defaultJsonDateFormat) {
        this.defaultJsonDateFormat = defaultJsonDateFormat;
    }

    public void addRequestInterceptor(RequestInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public void removeRequestInterceptor(RequestInterceptor interceptor) {
        this.interceptors.remove(interceptor);
    }
    public void addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot) {
        dynamicServiceRoots.add(dynamicServiceRoot);
    }
}
