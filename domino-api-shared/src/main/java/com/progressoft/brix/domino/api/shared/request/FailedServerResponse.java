package com.progressoft.brix.domino.api.shared.request;

public class FailedServerResponse extends ServerResponse {

    private static final long serialVersionUID = 7146258885910449957L;

    private Throwable error;

    public FailedServerResponse() {
        //Have to have this default constructor for GWT RPC serialization.
    }

    public FailedServerResponse(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
