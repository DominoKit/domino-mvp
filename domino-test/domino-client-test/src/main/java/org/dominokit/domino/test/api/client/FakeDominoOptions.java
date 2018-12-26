package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;
import org.dominokit.domino.api.client.DynamicServiceRoot;
import org.dominokit.domino.api.client.request.RequestInterceptor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class FakeDominoOptions implements DominoOptions {

    private String serviceRoot;
    private String dateFormat = "";
    private List<DynamicServiceRoot> dynamicServiceRoots = new ArrayList<>();
    private RequestInterceptor requestInterceptor = (request, callBack) -> callBack.onComplete();
    private int port;
    private ApplicationStartHandler applicationStartHandler;

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
    public CanSetDominoOptions setRequestInterceptor(RequestInterceptor interceptor) {
        this.requestInterceptor = interceptor;
        return this;
    }

    @Override
    public CanSetDominoOptions setApplicationStartHandler(ApplicationStartHandler applicationStartHandler) {
        this.applicationStartHandler = applicationStartHandler;
        return this;
    }

    @Override
    public RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
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
}
