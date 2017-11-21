package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.server.request.DefaultMultiValuesMap;
import com.progressoft.brix.domino.api.server.request.MultiValuesMap;
import com.progressoft.brix.domino.api.server.response.ResponseContext;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

public class TestResponseContext<S extends ResponseBean> implements ResponseContext<S> {
    private S responseBean;
    private String bodyString;
    private boolean ended;
    private int statusCode;
    private MultiValuesMap<String, String> headers = new DefaultMultiValuesMap<>();

    @Override
    public ResponseContext<S> putHeader(String name, String value) {
        headers.add(name, value);
        return this;
    }

    @Override
    public ResponseContext<S> putHeader(String name, Iterable<String> values) {
        headers.add(name, values);
        return this;
    }

    @Override
    public ResponseContext<S> statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @Override
    public void end() {
        this.ended = true;
    }

    @Override
    public void end(S body) {
        this.ended = true;
        responseBean = body;
    }

    @Override
    public void end(String body) {
        this.ended = true;
        this.bodyString = body;
    }

    public String getBodyString() {
        return bodyString;
    }

    public boolean isEnded() {
        return ended;
    }

    public MultiValuesMap<String, String> getHeaders() {
        return headers;
    }

    public S getResponseBean() {
        return responseBean;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
