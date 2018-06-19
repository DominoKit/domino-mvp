package org.dominokit.domino.gwt.client.options;

import org.dominokit.domino.api.client.CanSetDominoOptions;
import org.dominokit.domino.api.client.DominoOptions;
import org.dominokit.domino.api.client.DynamicServiceRoot;

import java.util.List;

public class DefaultOptions implements DominoOptions {
    @Override
    public void applyOptions() {

    }

    @Override
    public CanSetDominoOptions setDefaultServiceRoot(String defaultServiceRoot) {
        throw new NotImplementedException();
    }

    @Override
    public CanSetDominoOptions setDefaultJsonDateFormat(String defaultJsonDateFormat) {
        throw new NotImplementedException();
    }

    @Override
    public CanSetDominoOptions addDynamicServiceRoot(DynamicServiceRoot dynamicServiceRoot) {
        throw new NotImplementedException();
    }

    @Override
    public String getDefaultServiceRoot() {
        throw new NotImplementedException();
    }

    @Override
    public String getDefaultJsonDateFormat() {
        throw new NotImplementedException();
    }

    @Override
    public List<DynamicServiceRoot> getServiceRoots() {
        throw new NotImplementedException();
    }

    private final static class NotImplementedException extends RuntimeException{

    }
}
