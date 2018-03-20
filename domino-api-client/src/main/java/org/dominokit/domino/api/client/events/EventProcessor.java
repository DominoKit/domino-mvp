package org.dominokit.domino.api.client.events;

@FunctionalInterface
public interface EventProcessor {
    void process(Event event);
}
