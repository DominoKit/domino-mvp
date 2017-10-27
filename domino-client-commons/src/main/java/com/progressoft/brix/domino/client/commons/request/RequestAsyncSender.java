package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.request.ServerRequest;

@FunctionalInterface
public interface RequestAsyncSender {
    void send(ServerRequest request);
}
