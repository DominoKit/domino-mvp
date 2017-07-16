package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

@FunctionalInterface
public interface TestServerService {

    interface RequestExecutionCallBack{
        void onSuccess(ServerResponse response);
        void onFailed(ServerResponse response);
    }

    ServerResponse executeRequest(ServerRequest request);
}
