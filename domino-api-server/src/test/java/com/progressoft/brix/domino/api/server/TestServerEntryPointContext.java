package com.progressoft.brix.domino.api.server;

public class TestServerEntryPointContext implements ServerEntryPointContext{

    private final String entryContextParameter="-entry-point-parameter";

    public String getEntryContextParameter() {
        return entryContextParameter;
    }

    @Override
    public String getName() {
        return TestServerEntryPointContext.class.getCanonicalName();
    }
}
