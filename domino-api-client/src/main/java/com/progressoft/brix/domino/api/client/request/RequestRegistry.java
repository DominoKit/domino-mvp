package com.progressoft.brix.domino.api.client.request;

@FunctionalInterface
public interface RequestRegistry{
    void registerRequest(String requestName, String presenterName);
}
