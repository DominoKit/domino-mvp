package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.ResponseBean;

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

    public <R extends ServerRequest> boolean isSent(Class<R> request) {
        return receivedRequests.containsKey(request.getCanonicalName());
    }

    public <R extends ServerRequest> boolean isSent(Class<R> request, int executionCount) {
        return receivedRequests.containsKey(request.getCanonicalName()) && receivedRequests.get(request.getCanonicalName()).executionsCount==executionCount;
    }

    public <S extends ResponseBean, R extends ServerRequest> S getResponse(Class<R> request) {
        return (S) receivedRequests.get(request.getCanonicalName()).response;
    }

    public <R extends ServerRequest> R getRequest(Class<R> request) {
        return (R) receivedRequests.get(request.getCanonicalName()).request;
    }
}
