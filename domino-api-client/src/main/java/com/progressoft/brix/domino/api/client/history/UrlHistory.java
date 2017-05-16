package com.progressoft.brix.domino.api.client.history;

@FunctionalInterface
public interface UrlHistory {
    void apply(String urlToken);
}
