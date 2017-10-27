package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public interface Response<S extends ResponseBean> {
    CanFailOrSend onSuccess(Success<S> success);
}
