package com.progressoft.brix.domino.api.client.request;


import com.progressoft.brix.domino.api.shared.request.ServerRequest;

public interface RequestRestSender<T extends ServerRequest> {
    void send(T request, ServerRequestCallBack callBack);
}
