package com.progressoft.brix.domino.api.server.request;

import com.progressoft.brix.domino.api.server.context.ExecutionContext;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;

@FunctionalInterface
public interface RequestExecutor {
    void executeRequest(ExecutionContext requestContext, ServerEntryPointContext context);
}
