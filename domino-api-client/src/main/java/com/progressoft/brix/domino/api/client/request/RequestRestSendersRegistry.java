package com.progressoft.brix.domino.api.client.request;

@FunctionalInterface
public interface RequestRestSendersRegistry {
    void registerRequestRestSender(String requestName, LazyRequestRestSenderLoader loader);
}
