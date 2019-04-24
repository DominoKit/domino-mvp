package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.DominoOptions;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.api.shared.request.DynamicServiceRoot;
import org.dominokit.domino.api.shared.request.RequestInterceptor;
import org.dominokit.domino.api.shared.request.RequestRouter;
import org.dominokit.domino.api.shared.request.ServerRequest;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DesktopDominoOptions implements DominoOptions {

    private String defaultServiceRoot;
    private String defaultResourceRootPath = "service";
    private String defaultJsonDateFormat = null;
    private List<DynamicServiceRoot> dynamicServiceRoots = new ArrayList<>();
    private List<RequestInterceptor> interceptors = new ArrayList<>();
    private ApplicationStartHandler applicationStartHandler;

    @Override
    public void applyOptions() {
        //not implemented yet
    }

    @Override
    public CanSetDominoOptions setDefaultServiceRoot(String defaultServiceRoot) {
        this.defaultServiceRoot = defaultServiceRoot;
        return this;
    }

    @Override
    public CanSetDominoOptions setDefaultJsonDateFormat(String defaultJsonDateFormat) {
        this.defaultJsonDateFormat = defaultJsonDateFormat;
        return this;
    }

    @Override
    public CanSetDominoOptions addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot) {
        dynamicServiceRoots.add(dynamicServiceRoot);
        return this;
    }

    @Override
    public CanSetDominoOptions addRequestInterceptor(RequestInterceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    @Override
    public CanSetDominoOptions removeRequestInterceptor(RequestInterceptor interceptor) {
        this.interceptors.remove(interceptor);
        return this;
    }

    @Override
    public CanSetDominoOptions setApplicationStartHandler(ApplicationStartHandler applicationStartHandler) {
        this.applicationStartHandler = applicationStartHandler;
        return this;
    }

    @Override
    public CanSetDominoOptions setDefaultResourceRootPath(String rootPath) {
        if(nonNull(rootPath)){
            this.defaultResourceRootPath = rootPath;
        }
        return this;
    }

    @Override
    public List<RequestInterceptor> getRequestInterceptors() {
        return interceptors;
    }

    @Override
    public ApplicationStartHandler getApplicationStartHandler() {
        return applicationStartHandler;
    }

    @Override
    public String getDefaultResourceRootPath() {
        return defaultResourceRootPath;
    }

    @Override
    public String getDefaultServiceRoot() {
        if(isNull(defaultServiceRoot)){
            return "http://localhost:8080/"+ defaultResourceRootPath;
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
    public RequestRouter<PresenterCommand> getClientRouter() {
        return ClientApp.make().getClientRouter();
    }

    @Override
    public RequestRouter<ServerRequest> getServerRouter() {
        return ClientApp.make().getServerRouter();
    }
}
