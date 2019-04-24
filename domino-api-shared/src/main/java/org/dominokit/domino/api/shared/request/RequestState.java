package org.dominokit.domino.api.shared.request;

@FunctionalInterface
public interface RequestState<C extends RequestStateContext> {
    void execute(C request);
}
