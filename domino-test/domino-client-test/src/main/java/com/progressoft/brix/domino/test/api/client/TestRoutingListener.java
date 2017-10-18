package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

import java.util.HashMap;
import java.util.Map;

public class TestRoutingListener implements TestServerRouter.RoutingListener {

    private class RequestResponsePair {
        private ClientServerRequest request;
        private ServerResponse response;
        private int executionsCount;

        public RequestResponsePair(ClientServerRequest request, ServerResponse response) {
            this.request = request;
            this.response = response;
            this.executionsCount = 0;
        }

        public int getExecutionsCount() {
            return executionsCount;
        }

        private void increment(ClientServerRequest request, ServerResponse response) {
            this.request = request;
            this.response = response;
            this.executionsCount++;
        }
    }

    private Map<String, RequestResponsePair> receivedRequests = new HashMap<>();

    @Override
    public void onRouteRequest(ClientServerRequest request, ServerResponse response) {
        if (receivedRequests.containsKey(request.getClass().getCanonicalName()))
            receivedRequests.get(request.getClass().getCanonicalName()).increment(request, response);
        else
            receivedRequests.put(request.getClass().getCanonicalName(), new RequestResponsePair(request, response));
    }

    public <C extends ClientServerRequest> boolean isSent(Class<C> request) {
        return receivedRequests.containsKey(request.getCanonicalName());
    }

    public <C extends ClientServerRequest> boolean isSent(Class<C> request, int executionCount) {
        return receivedRequests.containsKey(request.getCanonicalName()) && receivedRequests.get(request.getCanonicalName()).executionsCount==executionCount;
    }

    public <S extends ServerResponse, C extends ClientServerRequest> S getResponse(Class<C> request) {
        return (S) receivedRequests.get(request.getCanonicalName()).response;
    }

    public <C extends ClientServerRequest> C getRequest(Class<C> request) {
        return (C) receivedRequests.get(request.getCanonicalName()).request;
    }
}
