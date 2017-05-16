package com.progressoft.brix.domino.api.client.events;

import com.progressoft.brix.domino.api.client.request.ClientRequest;

@FunctionalInterface
public interface ClientRequestEventFactory {
    Event make(ClientRequest request);
}
