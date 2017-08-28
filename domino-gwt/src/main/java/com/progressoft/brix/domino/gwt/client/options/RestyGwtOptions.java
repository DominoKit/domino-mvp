package com.progressoft.brix.domino.gwt.client.options;

import com.progressoft.brix.domino.api.client.CanSetDominoOptions;
import com.progressoft.brix.domino.api.client.DominoOptions;
import org.fusesource.restygwt.client.Defaults;

import static com.google.gwt.core.client.GWT.getModuleBaseURL;
import static com.google.gwt.core.client.GWT.getModuleName;

public class RestyGwtOptions implements DominoOptions{

    private String defaultServiceRoot=getModuleBaseURL()
            .replace("static/" + getModuleName() + "/", "") + "service";
    private String defaultJsonDateFormat =null;

    @Override
    public void applyOptions() {
        Defaults.setServiceRoot(defaultServiceRoot);
        Defaults.setDateFormat(defaultJsonDateFormat);
    }

    @Override
    public CanSetDominoOptions setDefaultServiceRoot(String defaultServiceRoot) {
        this.defaultServiceRoot=defaultServiceRoot;
        return this;
    }

    @Override
    public CanSetDominoOptions setDefaultJsonDateFormat(String defaultJsonDateFormat) {
        this.defaultJsonDateFormat=defaultJsonDateFormat;
        return this;
    }

    @Override
    public String getDefaultServiceRoot() {
        return defaultServiceRoot;
    }

    @Override
    public String getDefaultJsonDateFormat() {
        return defaultJsonDateFormat;
    }
}
