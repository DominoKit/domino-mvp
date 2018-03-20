package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.ResponseBean;

public interface ServerRequestCallBack {
    void onFailure(Throwable throwable);
    void onSuccess(ResponseBean response);
}
