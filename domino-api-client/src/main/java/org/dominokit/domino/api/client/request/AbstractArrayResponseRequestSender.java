package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.ArrayResponse;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

public abstract class AbstractArrayResponseRequestSender<R extends RequestBean, S extends ResponseBean> extends RequestSender<R, ArrayResponse<S>> {

//    @Override
//    protected void readResponse(ServerRequestCallBack callBack, Response response){
//        S[] items = getResponseMapper().readArray(response.getBodyAsString(), this::initArray);
//        callBack.onSuccess(new ArrayResponse<>(items));
//    }

//    protected abstract S[] initArray(int length);
//
//    protected abstract AbstractObjectMapper<S> getResponseMapper();

}

