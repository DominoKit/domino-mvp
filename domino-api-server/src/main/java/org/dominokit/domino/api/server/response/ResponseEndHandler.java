package org.dominokit.domino.api.server.response;

@FunctionalInterface
public interface ResponseEndHandler {
    void onResponseEnded();
}
