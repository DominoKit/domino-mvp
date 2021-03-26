/*
 * Copyright © ${year} Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.test.api.client;

import java.util.HashMap;
import java.util.Map;
import org.dominokit.domino.rest.shared.request.ServerRequest;

public class TestRoutingListener implements TestServerRouter.RoutingListener {

  private Map<String, RequestResponsePair> receivedRequests = new HashMap<>();

  private class RequestResponsePair {
    private ServerRequest request;
    private Object response;
    private int executionsCount;

    public RequestResponsePair(ServerRequest request, Object response) {
      this.request = request;
      this.response = response;
      this.executionsCount = 0;
    }

    public int getExecutionsCount() {
      return executionsCount;
    }

    private void increment(ServerRequest request, Object response) {
      this.request = request;
      this.response = response;
      this.executionsCount++;
    }
  }

  @Override
  public void onRouteRequest(ServerRequest request, Object response) {
    if (receivedRequests.containsKey(request.getClass().getCanonicalName()))
      receivedRequests.get(request.getClass().getCanonicalName()).increment(request, response);
    else
      receivedRequests.put(
          request.getClass().getCanonicalName(), new RequestResponsePair(request, response));
  }

  public <R extends ServerRequest> boolean isSent(Class<R> request) {
    return receivedRequests.containsKey(request.getCanonicalName());
  }

  public <R extends ServerRequest> boolean isSent(Class<R> request, int executionCount) {
    return receivedRequests.containsKey(request.getCanonicalName())
        && receivedRequests.get(request.getCanonicalName()).executionsCount == executionCount;
  }

  public <S, R extends ServerRequest> S getResponse(Class<R> request) {
    return (S) receivedRequests.get(request.getCanonicalName()).response;
  }

  public <R extends ServerRequest> R getRequest(Class<R> request) {
    return (R) receivedRequests.get(request.getCanonicalName()).request;
  }
}
