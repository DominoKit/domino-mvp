package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

import java.util.HashMap;
import java.util.Map;

public class TestRoutingListener implements TestServerRouter.RoutingListener {

    private class RequestResponsePair {
        private final ClientServerRequest request;
        private final ServerResponse response;

        public RequestResponsePair(ClientServerRequest request, ServerResponse response) {
            this.request = request;
            this.response = response;
        }
    }

    private Map<String, RequestResponsePair> receivedRequests = new HashMap<>();

    @Override
    public void onRouteRequest(ClientServerRequest request, ServerResponse response) {
        receivedRequests.put(request.getKey(), new RequestResponsePair(request, response));
    }

    public <C extends ClientServerRequest> boolean isSent(Class<C> request) {
        return receivedRequests.containsKey(request.getCanonicalName());
    }

    public <S extends ServerResponse, C extends ClientServerRequest> S getResponse(Class<C> request) {
        return (S) receivedRequests.get(request.getCanonicalName()).response;
    }

    public <C extends ClientServerRequest> C getRequest(Class<C> request) {
        return (C) receivedRequests.get(request.getCanonicalName()).request;
    }
}
