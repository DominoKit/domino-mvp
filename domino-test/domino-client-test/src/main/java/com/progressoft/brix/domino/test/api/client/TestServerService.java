package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface TestServerService {

    interface RequestExecutionCallBack {
        void onSuccess(ResponseBean response);

        void onFailed(ResponseBean response);
    }

    void executeRequest(RequestBean request, TestResponseContext responseContext);
}
