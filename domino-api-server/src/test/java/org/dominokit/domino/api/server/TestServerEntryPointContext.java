package org.dominokit.domino.api.server;

import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;

public class TestServerEntryPointContext implements ServerEntryPointContext {

    private final String entryContextParameter="-entry-point-parameter";

    public String getEntryContextParameter() {
        return entryContextParameter;
    }

    @Override
    public String getName() {
        return TestServerEntryPointContext.class.getCanonicalName();
    }
}
