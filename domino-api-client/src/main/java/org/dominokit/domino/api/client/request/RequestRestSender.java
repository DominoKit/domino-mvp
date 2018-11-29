package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

public interface RequestRestSender<T extends RequestBean, S extends ResponseBean> {
    void send(ServerRequest<T, S> request, ServerRequestCallBack callBack);
}
