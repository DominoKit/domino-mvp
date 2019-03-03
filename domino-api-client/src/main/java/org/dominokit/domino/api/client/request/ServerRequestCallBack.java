package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.FailedResponseBean;

public interface ServerRequestCallBack {
    void onFailure(FailedResponseBean failedResponse);
    <T> void  onSuccess(T response);
}
