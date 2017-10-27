package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;

import java.util.HashMap;
import java.util.Map;

public class TestRoutingListener implements TestServerRouter.RoutingListener {

    private Map<String, RequestResponsePair> receivedRequests = new HashMap<>();

    private class RequestResponsePair {
        private ServerRequest request;
        private ResponseBean response;
        private int executionsCount;


        public RequestResponsePair(ServerRequest request, ResponseBean response) {
            this.request = request;
            this.response = response;
            this.executionsCount = 0;
        }

        public int getExecutionsCount() {
            return executionsCount;
        }
        private void increment(ServerRequest request, ResponseBean response) {
            this.request = request;
            this.response = response;
            this.executionsCount++;
        }

    }

    @Override
    public void onRouteRequest(ServerRequest request, ResponseBean response) {
        if (receivedRequests.containsKey(request.getClass().getCanonicalName()))
            receivedRequests.get(request.getClass().getCanonicalName()).increment(request, response);
        else
            receivedRequests.put(request.getClass().getCanonicalName(), new RequestResponsePair(request, response));
    }

    public <C extends ServerRequest> boolean isSent(Class<C> request) {
        return receivedRequests.containsKey(request.getCanonicalName());
    }

    public <C extends ServerRequest> boolean isSent(Class<C> request, int executionCount) {
        return receivedRequests.containsKey(request.getCanonicalName()) && receivedRequests.get(request.getCanonicalName()).executionsCount==executionCount;
    }

    public <S extends ResponseBean, C extends ServerRequest> S getResponse(Class<C> request) {
        return (S) receivedRequests.get(request.getCanonicalName()).response;
    }

    public <C extends ServerRequest> C getRequest(Class<C> request) {
        return (C) receivedRequests.get(request.getCanonicalName()).request;
    }
}
