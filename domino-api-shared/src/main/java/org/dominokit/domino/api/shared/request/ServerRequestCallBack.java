package org.dominokit.domino.api.shared.request;

public interface ServerRequestCallBack {
    void onFailure(FailedResponseBean failedResponse);
    <T> void  onSuccess(T response);
}
