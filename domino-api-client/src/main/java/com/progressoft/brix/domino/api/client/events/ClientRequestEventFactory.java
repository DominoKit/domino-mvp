package com.progressoft.brix.domino.api.client.events;

import com.progressoft.brix.domino.api.client.request.PresenterCommand;

@FunctionalInterface
public interface ClientRequestEventFactory {
    Event make(PresenterCommand request);
}
