package org.dominokit.domino.api.shared.request;

import java.util.Map;

public class FailedResponseBean implements ResponseBean {

    private static final long serialVersionUID = 7146258885910449957L;

    private int statusCode;
    private String statusText;
    private String body;
    private Map<String, String> headers;
    private Throwable throwable;

    public FailedResponseBean(Throwable throwable) {
        this.throwable = throwable;
    }

    public FailedResponseBean(int statusCode, String statusText, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public String toString() {
        return "FailedResponseBean{" +
                "statusCode=" + statusCode +
                ", statusText='" + statusText + '\'' +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                ", throwable=" + throwable +
                '}';
    }
}
