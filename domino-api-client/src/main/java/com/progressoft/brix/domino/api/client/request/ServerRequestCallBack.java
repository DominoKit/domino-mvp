package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.shared.request.ServerResponse;

public interface ServerRequestCallBack {
    void onFailure(Throwable throwable);
    void onSuccess(ServerResponse response);
}
