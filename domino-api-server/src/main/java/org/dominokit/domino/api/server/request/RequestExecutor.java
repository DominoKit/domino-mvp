package org.dominokit.domino.api.server.request;

import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.context.ExecutionContext;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;

@FunctionalInterface
public interface RequestExecutor {
    void executeRequest(ExecutionContext requestContext, ServerEntryPointContext context);
}
