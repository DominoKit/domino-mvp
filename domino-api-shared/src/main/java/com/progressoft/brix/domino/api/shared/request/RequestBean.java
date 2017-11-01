package com.progressoft.brix.domino.api.shared.request;

import java.io.Serializable;

public class RequestBean implements Serializable{

    private static final long serialVersionUID = -8670728823544377945L;
    public static final VoidRequest VOID_REQUEST = new VoidRequest();

    private String requestKey;

    public RequestBean() {
        this.requestKey = this.getClass().getCanonicalName();
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }
}
