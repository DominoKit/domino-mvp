package com.progressoft.brix.domino.gwt.client.request;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Cookies;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Method;

public class DominoRequestDispatcher implements Dispatcher {
    @Override
    public Request send(Method method, RequestBuilder builder) throws RequestException {
        builder.setHeader("X-XSRF-TOKEN", Cookies.getCookie("XSRF-TOKEN"));
        return builder.send();
    }
}
