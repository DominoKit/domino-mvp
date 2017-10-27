package com.progressoft.brix.domino.api.client.request;


import com.progressoft.brix.domino.api.shared.request.RequestBean;

import java.util.Map;

public interface RequestRestSender<T extends RequestBean> {
    void send(T request, Map<String, String> headers, ServerRequestCallBack callBack);
}
