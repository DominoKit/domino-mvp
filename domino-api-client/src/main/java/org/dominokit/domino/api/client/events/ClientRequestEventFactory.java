package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.rest.shared.Event;

@FunctionalInterface
public interface ClientRequestEventFactory {
    Event make(PresenterCommand request);
}
