package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.client.history.UrlHistory;

public class TestUrlHistory implements UrlHistory{

    private String token;

    @Override
    public void apply(String urlToken) {
        token=urlToken;
    }

    public String getToken() {
        return token;
    }
}
