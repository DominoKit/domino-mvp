package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface TestServerService {

    interface RequestExecutionCallBack {
        void onSuccess(ResponseBean response);

        void onFailed(ResponseBean response);
    }

    void executeRequest(RequestBean request, TestResponseContext responseContext);
}
