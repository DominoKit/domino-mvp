package com.progressoft.brix.domino.api.shared.request;

//import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

//@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY )
public class ServerRequest implements Serializable{

    private static final long serialVersionUID = -8670728823544377945L;

    private String requestKey;

    public ServerRequest() {
        //Have to have this default constructor for GWT RPC serialization.
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }
}
