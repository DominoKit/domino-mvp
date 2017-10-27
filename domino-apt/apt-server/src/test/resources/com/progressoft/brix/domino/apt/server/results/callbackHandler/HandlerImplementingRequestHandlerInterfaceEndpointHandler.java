package com.progressoft.brix.domino.apt.server.callbackHandler;

import com.progressoft.brix.domino.api.server.endpoint.AbstractEndpointCallBackHandler;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.EndpointsProcessor")
public class HandlerImplementingRequestHandlerInterfaceEndpointHandler extends AbstractEndpointCallBackHandler<RequestBean, ResponseBean> {
    @Override
    protected RequestBean makeNewRequest() {
        return new RequestBean();
    }

    @Override
    protected Class<RequestBean> getRequestClass() {
        return RequestBean.class;
    }
}