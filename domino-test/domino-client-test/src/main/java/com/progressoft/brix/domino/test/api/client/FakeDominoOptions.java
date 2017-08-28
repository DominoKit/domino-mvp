package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.CanSetDominoOptions;
import com.progressoft.brix.domino.api.client.DominoOptions;

public class FakeDominoOptions implements DominoOptions{

    private String serviceRoot="";
    private String dateFormat="";

    @Override
    public void applyOptions() {
        //no need to apply things now
    }

    @Override
    public CanSetDominoOptions setDefaultServiceRoot(String defaultServiceRoot) {
        this.serviceRoot=defaultServiceRoot;
        return this;
    }

    @Override
    public CanSetDominoOptions setDefaultJsonDateFormat(String defaultJsonDateFormat) {
        this.dateFormat=defaultJsonDateFormat;
        return this;
    }

    @Override
    public String getDefaultServiceRoot() {
        return serviceRoot;
    }

    @Override
    public String getDefaultJsonDateFormat() {
        return dateFormat;
    }
}
