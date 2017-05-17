package com.progressoft.brix.domino.api.server.handler;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

@FunctionalInterface
public interface RequestHandler<R extends ServerRequest, S extends ServerResponse> {
    S handleRequest(R arguments);
}
