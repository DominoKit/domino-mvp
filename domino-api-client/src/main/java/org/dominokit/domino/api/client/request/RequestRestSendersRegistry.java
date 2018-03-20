package org.dominokit.domino.api.client.request;

@FunctionalInterface
public interface RequestRestSendersRegistry {
    void registerRequestRestSender(String requestName, LazyRequestRestSenderLoader loader);
}
