package org.dominokit.domino.gwt.client.options;

import com.google.gwt.core.client.GWT;
import org.dominokit.domino.api.client.ApplicationStartHandler;
import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;
import org.dominokit.domino.api.client.DynamicServiceRoot;
import org.dominokit.domino.api.client.request.RequestInterceptor;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultDominoOptions implements DominoOptions {
    private String defaultServiceRoot;
    private String defaultResourceRootPath = "service";
    private String defaultJsonDateFormat = null;
    private List<DynamicServiceRoot> dynamicServiceRoots = new ArrayList<>();
    private RequestInterceptor requestInterceptor = (request, callBack) -> callBack.onComplete();
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
        if(isNull(defaultServiceRoot)){
            return GWT.getModuleBaseURL().replace("static", defaultResourceRootPath);
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
    public CanSetDominoOptions setDefaultResourceRootPath(String rootPath) {
        if(nonNull(rootPath)) {
            this.defaultResourceRootPath = rootPath;
        }
        return this;
    }

    @Override
    public String getDefaultResourceRootPath() {
        return defaultResourceRootPath;
    }
}
