package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

public interface ServerRequestCallBack {
    void onFailure(FailedResponseBean failedResponse);
    <T> void  onSuccess(T response);
}
