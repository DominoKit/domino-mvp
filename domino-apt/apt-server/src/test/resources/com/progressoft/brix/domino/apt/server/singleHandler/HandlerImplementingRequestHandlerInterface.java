package com.progressoft.brix.domino.apt.server.singleHandler;

import com.progressoft.brix.domino.api.server.request.RequestContext;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.api.server.handler.Handler;
import com.progressoft.brix.domino.api.server.handler.RequestHandler;

@Handler("somePath")
public class HandlerImplementingRequestHandlerInterface implements RequestHandler<RequestBean, ResponseBean>{
    @Override
    public ResponseBean handleRequest(RequestContext<RequestBean> requestContext) {
        return null;
    }

}
