package org.dominokit.domino.gwt.client.request;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Cookies;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;

import java.util.HashMap;
import java.util.Map;

public class DominoRequestDispatcher implements Dispatcher {
    private Map<String, String> headers = new HashMap<>();

    @Override
    public Request send(Method method, RequestBuilder builder) throws RequestException {
        builder.setHeader("X-XSRF-TOKEN", Cookies.getCookie("XSRF-TOKEN"));
        setHeaders(builder);
        return builder.send();
    }

    private void setHeaders(RequestBuilder builder) {
        headers.forEach(builder::setHeader);
    }

    public void withHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
