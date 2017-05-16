package com.progressoft.brix.domino.api.client.events;

@FunctionalInterface
public interface EventProcessor {
    void process(Event event);
}
