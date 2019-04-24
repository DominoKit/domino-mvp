package org.dominokit.domino.test.api.client;

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

public class FakeDominoOptions implements DominoOptions {

    private String serviceRoot;
    private String resourceRootPath;
    private String dateFormat = "";
    private List<DynamicServiceRoot> dynamicServiceRoots = new ArrayList<>();
    private List<RequestInterceptor> interceptors = new ArrayList<>();
    private int port;
    private ApplicationStartHandler applicationStartHandler;

    @Override
    public CanSetDominoOptions setDefaultResourceRootPath(String rootPath) {
        this.resourceRootPath = rootPath;
        return this;
    }

    @Override
    public String getDefaultResourceRootPath() {
        return resourceRootPath;
    }

    @Override
    public void applyOptions() {
        //no need to apply things now
    }

    @Override
    public CanSetDominoOptions setDefaultServiceRoot(String defaultServiceRoot) {
        this.serviceRoot = defaultServiceRoot;
        return this;
    }

    @Override
    public CanSetDominoOptions setDefaultJsonDateFormat(String defaultJsonDateFormat) {
        this.dateFormat = defaultJsonDateFormat;
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
    public List<RequestInterceptor> getRequestInterceptors() {
        return interceptors;
    }

    @Override
    public ApplicationStartHandler getApplicationStartHandler() {
        return applicationStartHandler;
    }

    @Override
    public String getDefaultServiceRoot() {
        return isNull(serviceRoot) ? "http://localhost:" + port + "/service/" : serviceRoot;
    }

    @Override
    public String getDefaultJsonDateFormat() {
        return dateFormat;
    }

    @Override
    public List<DynamicServiceRoot> getServiceRoots() {
        return dynamicServiceRoots;
    }

    public void setPort(int port) {
        this.port = port;
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
