package com.progressoft.brix.domino.gwt.client.request;

import com.progressoft.brix.domino.api.client.request.RequestHolder;
import com.progressoft.brix.domino.api.client.request.RequestsRepository;

import java.util.HashMap;

public class InMemoryRequestsRepository implements RequestsRepository {

    private final HashMap<String, RequestHolder> requestPresenterWrappers = new HashMap<>();

    @Override
    public void registerRequest(RequestHolder requestHolder) {
        if (isRegisteredRequest(requestHolder.getRequestName()))
            throw new RequestCannotBeRegisteredMoreThanOnce("Request key [" + requestHolder.getRequestName() + "]");
        requestPresenterWrappers.put(requestHolder.getRequestName(), requestHolder);
    }

    @Override
    public RequestHolder findRequestPresenterWrapper(String requestName) {
        if (isRegisteredRequest(requestName))
            return requestPresenterWrappers.get(requestName);
        throw new RequestKeyNotFoundException("Request Key [" + requestName + "]");
    }

    @Override
    public void clear() {
        requestPresenterWrappers.clear();
    }

    private boolean isRegisteredRequest(String requestName) {
        return requestPresenterWrappers.containsKey(requestName);
    }
}
