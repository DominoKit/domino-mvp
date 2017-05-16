package com.progressoft.brix.domino.api.client.request;

@FunctionalInterface
public interface RequestState<C extends RequestStateContext> {
    void execute(C request);
}
