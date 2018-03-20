package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.FailedResponseBean;

@FunctionalInterface
public interface Fail {
    void onFail(FailedResponseBean failedResponse);
}
