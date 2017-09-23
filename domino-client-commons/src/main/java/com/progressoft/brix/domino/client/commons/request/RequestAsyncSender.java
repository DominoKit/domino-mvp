package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;

@FunctionalInterface
public interface RequestAsyncSender {
    void send(ClientServerRequest request);

    default void onBeforeSend(ClientServerRequest request) {
    }
}
