package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.client.request.PresenterCommand;

@FunctionalInterface
public interface ClientRequestEventFactory {
    Event make(PresenterCommand request);
}
