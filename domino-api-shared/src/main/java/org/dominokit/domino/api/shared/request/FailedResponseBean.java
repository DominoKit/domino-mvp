package org.dominokit.domino.api.shared.request;

public class FailedResponseBean implements ResponseBean {

    private static final long serialVersionUID = 7146258885910449957L;

    private Throwable error;

    public FailedResponseBean() {
        //Have to have this default constructor for GWT RPC serialization.
    }

    public FailedResponseBean(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
