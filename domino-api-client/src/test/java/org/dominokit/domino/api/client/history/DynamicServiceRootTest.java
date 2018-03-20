package org.dominokit.domino.api.client.history;

import org.dominokit.domino.api.client.DynamicServiceRoot;
import org.junit.Test;

public class DynamicServiceRootTest {

    @Test
    public void canCreateDynamicServiceRoot() throws Exception {
        DynamicServiceRoot.pathMatcher(path -> path.startsWith("countries")).serviceRoot(() -> "http://localhost:8080");
    }
}
