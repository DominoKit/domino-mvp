package org.dominokit.domino.client.commons.request;


import org.dominokit.domino.api.client.request.LazyRequestRestSenderLoader;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.RequestRestSendersRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRequestRestSendersRepository implements RequestRestSendersRepository {

    private final Map<String, LazyRequestRestSenderLoader> senders = new HashMap<>();

    @Override
    public void registerSender(String requestName, LazyRequestRestSenderLoader loader) {
        if (isRegisteredSender(requestName))
            throw new RequestRestSendersRepository.SenderCannotBeRegisteredMoreThanOnce(requestName);
        senders.put(requestName, loader);
    }

    private boolean isRegisteredSender(String requestName) {
        return senders.containsKey(requestName);
    }

    @Override
    public RequestRestSender get(String requestName) {
        if (isRegisteredSender(requestName))
            return senders.get(requestName).get();
        throw new RequestRestSendersRepository.SenderNotFoundException(requestName);
    }

    @Override
    public void clear() {
        senders.clear();
    }
}
