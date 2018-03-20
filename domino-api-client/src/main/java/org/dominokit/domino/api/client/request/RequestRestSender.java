package org.dominokit.domino.api.client.request;


import org.dominokit.domino.api.shared.request.RequestBean;

import java.util.Map;

public interface RequestRestSender<T extends RequestBean> {
    void send(T request, Map<String, String> headers, ServerRequestCallBack callBack);
}
