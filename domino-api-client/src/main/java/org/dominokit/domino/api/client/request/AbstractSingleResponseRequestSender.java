package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.api.shared.request.VoidResponse;
import org.dominokit.jacksonapt.AbstractObjectMapper;
import org.dominokit.rest.client.FailedResponse;
import org.dominokit.rest.shared.Response;
import org.dominokit.rest.shared.RestfulRequest;

import java.util.Arrays;

import static java.util.Objects.isNull;

public abstract class AbstractSingleResponseRequestSender<R extends RequestBean, S extends ResponseBean> extends AbstractRequestSender<R, S> {

    @Override
    protected void readResponse(ServerRequestCallBack callBack, Response response){
        callBack.onSuccess(getResponseMapper().read(response.getBodyAsString()));
    }

}

