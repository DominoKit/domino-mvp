package org.dominokit.domino.api.client.request;

public interface RequestRestSender<T, S > {
    void send(ServerRequest<T, S> request, ServerRequestCallBack callBack);
}
