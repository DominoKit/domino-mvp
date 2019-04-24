package org.dominokit.domino.api.shared.request;

@FunctionalInterface
public interface Success<S > {
    void onSuccess(S response);
}
