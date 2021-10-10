/*
 * Copyright © 2019 Dominokit
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

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.HashMap;
import java.util.Map;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.server.resource.ResourcesRepository;
import org.dominokit.rest.shared.Event;
import org.dominokit.rest.shared.EventsBus;
import org.dominokit.rest.shared.request.*;
import org.dominokit.rest.shared.request.Request.ServerFailedRequestStateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestServerRouter implements RequestRouter<ServerRequest> {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestServerRouter.class);

  private Map<String, ResponseReply> fakeResponses = new HashMap<>();
  private Map<String, Future<ResponseReply>> requestCompleteHandlers = new HashMap<>();
  private final RequestAsyncSender requestAsyncRunner;
  private TestRoutingListener defaultListener = new TestRoutingListener();
  private RoutingListener listener = defaultListener;

  private final ServerRequestEventFactory eventFactory =
      new ServerRequestEventFactory() {
        @Override
        public <T> Event makeSuccess(ServerRequest request, T responseBean) {
          defaultListener.onRouteRequest(request, responseBean);
          return new TestServerSuccessEvent(request, responseBean);
        }

        @Override
        public Event makeFailed(ServerRequest request, FailedResponseBean failedResponseBean) {
          defaultListener.onRouteRequest(request, failedResponseBean);
          return new TestServerFailedEvent(request, failedResponseBean);
        }
      };

  public TestServerRouter(Vertx vertx) {
    this.requestAsyncRunner = new TestRequestAsyncSender(eventFactory);
  }

  public void setRoutingListener(RoutingListener listener) {
    this.listener = listener;
  }

  public void removeRoutingListener() {
    this.listener = defaultListener;
  }

  @Override
  public void routeRequest(ServerRequest request) {
    Object response;
    try {
      if (fakeResponses.containsKey(getRequestKey(request))) {
        response = fakeResponses.get(getRequestKey(request)).reply();
        eventFactory.makeSuccess(request, response).fire();
      } else {

        requestAsyncRunner.send(request);
      }
    } catch (ResourcesRepository.RequestHandlerNotFound ex) {
      LOGGER.error(
          "Request resource not found for request ["
              + request.getClass().getSimpleName()
              + "]! either fake the request or start an actual server");
      eventFactory.makeFailed(request, new FailedResponseBean(ex)).fire();
    } catch (FakeRequestFailure ex) {
      LOGGER.error("could not execute request : ", ex);
      eventFactory.makeFailed(request, ex.failedResponseBean).fire();
    } catch (Exception ex) {
      LOGGER.error("could not execute request : ", ex);
      eventFactory.makeFailed(request, new FailedResponseBean(ex)).fire();
    }
  }

  private String getRequestKey(ServerRequest request) {
    return request.getClass().getCanonicalName();
  }

  private String getRequestKey(Class<? extends ServerRequest> request) {
    return request.getCanonicalName();
  }

  public void fakeResponse(String requestKey, ResponseReply reply) {
    fakeResponses.put(requestKey, reply);
  }

  public TestRoutingListener getDefaultRoutingListener() {
    return defaultListener;
  }

  public void onRequestCompleted(
      Class<? extends ServerRequest> request, Future<ResponseReply> completeHandler) {
    requestCompleteHandlers.put(getRequestKey(request), completeHandler);
  }

  public class TestServerSuccessEvent<T> implements Event {
    protected final ServerRequest request;
    protected final T responseBean;
    private final ClientApp clientApp = ClientApp.make();

    public TestServerSuccessEvent(ServerRequest request, T responseBean) {
      this.request = request;
      this.responseBean = responseBean;
    }

    @Override
    public void fire() {
      clientApp.getEventsBus().publishEvent(new TestSuccessRequestEvent(this));
    }

    @Override
    public void process() {
      request.applyState(new Request.ServerResponseReceivedStateContext(makeSuccessContext()));
      completeSuccessRequest(request, new SuccessReply(responseBean));
    }

    private Request.ServerSuccessRequestStateContext makeSuccessContext() {
      return new Request.ServerSuccessRequestStateContext(responseBean);
    }
  }

  private void completeSuccessRequest(ServerRequest request, ResponseReply reply) {
    String requestKey = getRequestKey(request);
    if (requestCompleteHandlers.containsKey(requestKey)) {
      requestCompleteHandlers.get(requestKey).complete(reply);
      requestCompleteHandlers.remove(requestKey);
    }
  }

  public class TestSuccessRequestEvent implements EventsBus.RequestEvent<TestServerSuccessEvent> {

    private final TestServerSuccessEvent event;

    public TestSuccessRequestEvent(TestServerSuccessEvent event) {
      this.event = event;
    }

    @Override
    public TestServerSuccessEvent asEvent() {
      return event;
    }
  }

  public class TestServerFailedEvent implements Event {
    protected final ServerRequest request;
    protected final FailedResponseBean failedResponseBean;
    private final ClientApp clientApp = ClientApp.make();

    public TestServerFailedEvent(ServerRequest request, FailedResponseBean failedResponseBean) {
      this.request = request;
      this.failedResponseBean = failedResponseBean;
    }

    @Override
    public void fire() {
      clientApp.getEventsBus().publishEvent(new TestFailedRequestEvent(this));
    }

    @Override
    public void process() {
      request.applyState(new Request.ServerResponseReceivedStateContext(makeFailedContext()));
      completeFailRequest(request, new FailedReply(failedResponseBean));
    }

    private ServerFailedRequestStateContext makeFailedContext() {
      return new ServerFailedRequestStateContext(failedResponseBean);
    }
  }

  private void completeFailRequest(ServerRequest request, FailedReply reply) {
    String requestKey = getRequestKey(request);
    if (requestCompleteHandlers.containsKey(requestKey)) {
      requestCompleteHandlers.get(requestKey).fail(new TestFailedRequestException(reply));
      requestCompleteHandlers.remove(requestKey);
    }
  }

  public class TestFailedRequestEvent implements EventsBus.RequestEvent<TestServerFailedEvent> {

    private final TestServerFailedEvent event;

    public TestFailedRequestEvent(TestServerFailedEvent event) {
      this.event = event;
    }

    @Override
    public TestServerFailedEvent asEvent() {
      return event;
    }
  }

  public interface RoutingListener {
    void onRouteRequest(ServerRequest request, Object response);
  }

  public interface ResponseReply {
    <T> T reply();
  }

  public static class SuccessReply<T> implements ResponseReply {
    private final T response;

    public SuccessReply(T response) {
      this.response = response;
    }

    @Override
    public T reply() {
      return response;
    }
  }

  public static class FailedReply implements ResponseReply {
    private final FailedResponseBean failedResponseBean;

    public FailedReply(FailedResponseBean failedResponseBean) {
      this.failedResponseBean = failedResponseBean;
    }

    @Override
    public ResponseBean reply() {
      throw new FakeRequestFailure(failedResponseBean);
    }
  }

  private static class FakeRequestFailure extends RuntimeException {
    private final FailedResponseBean failedResponseBean;

    public FakeRequestFailure(FailedResponseBean failedResponseBean) {
      this.failedResponseBean = failedResponseBean;
    }
  }

  public static final class TestFailedRequestException extends Exception {
    private final FailedReply failedReply;

    public TestFailedRequestException(FailedReply failedReply) {
      this.failedReply = failedReply;
    }

    public FailedReply getFailedReply() {
      return failedReply;
    }
  }
}
