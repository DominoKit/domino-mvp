package org.dominokit.domino.test.api.client;


import org.dominokit.domino.rest.shared.request.FailedResponseBean;
import org.dominokit.domino.rest.shared.Response;
import org.dominokit.domino.rest.shared.request.ServerRequest;

import java.util.Map;

public class TestFailedResponseBean extends FailedResponseBean {

    public TestFailedResponseBean() {
    }

    public TestFailedResponseBean(Throwable throwable) {
        super(throwable);
    }

    public <R, S> TestFailedResponseBean(ServerRequest<R, S> request, Response response) {
        super(request, response);
    }

    public void setStatusCode(int statusCode) {
        super.setStatusCode(statusCode);
    }

    public void setStatusText(String statusText) {
        setStatusText(statusText);
    }

    public void setBody(String body) {
        super.setBody(body);
    }

    public void setHeaders(Map<String, String> headers) {
        super.setHeaders(headers);
    }

    public void setThrowable(Throwable throwable) {
        super.setThrowable(throwable);
    }
}
