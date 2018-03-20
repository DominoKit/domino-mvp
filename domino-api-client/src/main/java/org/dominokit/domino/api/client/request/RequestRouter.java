package org.dominokit.domino.api.client.request;


@FunctionalInterface
public interface RequestRouter<R extends Request> {
    void routeRequest(R request);
}
