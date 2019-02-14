package org.dominokit.domino.api.client.request;

@FunctionalInterface
public interface Success<S > {
    void onSuccess(S response);
}
