package com.progressoft.brix.domino.api.client.history;

import com.progressoft.brix.domino.api.client.request.Request;

@FunctionalInterface
public interface RequestFromPath<R extends Request> {

    R buildRequest(TokenizedPath path);
}
