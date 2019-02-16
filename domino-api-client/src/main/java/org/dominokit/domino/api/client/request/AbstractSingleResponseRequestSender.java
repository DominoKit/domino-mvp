package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

public abstract class AbstractSingleResponseRequestSender<R extends RequestBean, S extends ResponseBean> extends RequestSender<R, S> {

//    @Override
//    protected void readResponse(ServerRequestCallBack callBack, Response response){
//        callBack.onSuccess(getResponseMapper().read(response.getBodyAsString()));
//    }

//    protected abstract AbstractObjectMapper<S> getResponseMapper();
}

