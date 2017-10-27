package com.progressoft.brix.domino.client.commons.request;

import com.progressoft.brix.domino.api.client.request.CommandsRepository;
import com.progressoft.brix.domino.api.client.request.RequestHolder;

import java.util.HashMap;

public class InMemoryCommandsRepository implements CommandsRepository {

    private final HashMap<String, RequestHolder> requestPresenterWrappers = new HashMap<>();

    @Override
    public void registerCommand(RequestHolder requestHolder) {
        if (isRegisteredRequest(requestHolder.getRequestName()))
            throw new CommandCannotBeRegisteredMoreThanOnce("Request key [" + requestHolder.getRequestName() + "]");
        requestPresenterWrappers.put(requestHolder.getRequestName(), requestHolder);
    }

    @Override
    public RequestHolder findRequestPresenterWrapper(String requestName) {
        if (isRegisteredRequest(requestName))
            return requestPresenterWrappers.get(requestName);
        throw new CommandKeyNotFoundException("Request Key [" + requestName + "]");
    }

    @Override
    public void clear() {
        requestPresenterWrappers.clear();
    }

    private boolean isRegisteredRequest(String requestName) {
        return requestPresenterWrappers.containsKey(requestName);
    }
}
