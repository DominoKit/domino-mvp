package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;

public interface ServerFailedHandler<P extends Presentable, R extends ServerRequest> {

    default void processFailed(P presenter, R serverArgs, FailedServerResponse failedResponse){
    }
}
