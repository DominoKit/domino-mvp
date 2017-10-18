package com.progressoft.brix.domino.api.client.request;


import com.progressoft.brix.domino.api.shared.request.ServerRequest;

import java.util.Map;

public interface RequestRestSender<T extends ServerRequest> {
    void send(T request, Map<String, String> headers, ServerRequestCallBack callBack);
}
