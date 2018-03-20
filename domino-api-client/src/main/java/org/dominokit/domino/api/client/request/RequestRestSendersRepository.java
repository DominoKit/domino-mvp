package org.dominokit.domino.api.client.request;

public interface RequestRestSendersRepository {
    void registerSender(String requestName, LazyRequestRestSenderLoader loader);
    RequestRestSender get(String requestName);
    void clear();

    class SenderCannotBeRegisteredMoreThanOnce extends RuntimeException {
        public SenderCannotBeRegisteredMoreThanOnce(String requestName) {
            super(requestName);
        }
    }

    class SenderNotFoundException extends RuntimeException {
        public SenderNotFoundException(String requestName) {
            super(requestName);
        }
    }
}
