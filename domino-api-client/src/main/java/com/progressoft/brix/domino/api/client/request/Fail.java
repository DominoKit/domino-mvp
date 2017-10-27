package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.shared.request.FailedResponseBean;

@FunctionalInterface
public interface Fail {
    void onFail(FailedResponseBean failedResponse);
}
