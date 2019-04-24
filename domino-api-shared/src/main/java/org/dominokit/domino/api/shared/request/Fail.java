package org.dominokit.domino.api.shared.request;

@FunctionalInterface
public interface Fail {
    void onFail(FailedResponseBean failedResponse);
}
