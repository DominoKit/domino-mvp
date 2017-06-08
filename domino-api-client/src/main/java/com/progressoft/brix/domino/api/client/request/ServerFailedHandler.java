package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ServerFailedHandler<P extends Presentable, R extends ServerRequest> {

    Logger LOGGER= LoggerFactory.getLogger(ServerFailedHandler.class);
    default void processFailed(P presenter, R serverArgs, FailedServerResponse failedResponse){
        LOGGER.error("could not execute request on server: ", failedResponse.getError());
    }
}
