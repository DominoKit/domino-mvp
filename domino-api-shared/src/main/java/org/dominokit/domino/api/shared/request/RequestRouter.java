package org.dominokit.domino.api.shared.request;


@FunctionalInterface
public interface RequestRouter<R extends Request> {
    void routeRequest(R request);
}
