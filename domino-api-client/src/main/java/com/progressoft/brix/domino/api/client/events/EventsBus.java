package com.progressoft.brix.domino.api.client.events;

@FunctionalInterface
public interface EventsBus<T>{

    @FunctionalInterface
    interface RequestEvent<T>{
        T asEvent();
    }

    void publishEvent(RequestEvent<T> event);
}
