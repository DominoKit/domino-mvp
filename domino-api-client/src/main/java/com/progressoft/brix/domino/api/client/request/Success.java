package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface Success<S extends ResponseBean> {
    void onSuccess(S response);
}
