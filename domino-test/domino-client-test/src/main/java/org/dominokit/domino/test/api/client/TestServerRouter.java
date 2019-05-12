package org.dominokit.domino.test.api.client;

import io.vertx.core.Vertx;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.EventsBus;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.server.resource.ResourcesRepository;
import org.dominokit.domino.api.shared.request.*;
import org.dominokit.domino.client.commons.request.RequestAsyncSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TestServerRouter implements RequestRouter<ServerRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServerRouter.class);

    private Map<String, ResponseReply> fakeResponses = new HashMap<>();
    private Map<String, DominoTestClient.RequestCompleteHandler> requestSuccessCompleteHandlers = new HashMap<>();
    private Map<String, DominoTestClient.RequestCompleteHandler> requestFailCompleteHandlers = new HashMap<>();
    private final RequestAsyncSender requestAsyncRunner;
    private TestRoutingListener defaultListener = new TestRoutingListener();
    private RoutingListener listener = defaultListener;

    private final ServerRequestEventFactory eventFactory = new ServerRequestEventFactory() {
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
        ResponseBean response;
        try {
            if (fakeResponses.containsKey(getRequestKey(request))) {
                response = fakeResponses.get(getRequestKey(request)).reply();
                eventFactory.makeSuccess(request, response).fire();
            } else {

                requestAsyncRunner.send(request);
            }
        } catch (ResourcesRepository.RequestHandlerNotFound ex) {
            LOGGER.error("Request resource not found for request [" + request.getClass().getSimpleName() + "]! either fake the request or start an actual server");
            eventFactory.makeFailed(request, new FailedResponseBean(ex)).fire();
        } catch (Exception ex) {
            LOGGER.error("could not execute request : ", ex);
            if (ex instanceof FakeRequestFailure) {
                eventFactory.makeFailed(request, ((FakeRequestFailure) ex).failedResponseBean).fire();
            } else {
                eventFactory.makeFailed(request, new FailedResponseBean(ex)).fire();
            }
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

    public void addRequestSuccessCompleteHandler(Class<? extends ServerRequest> request, DominoTestClient.RequestCompleteHandler completeHandler) {
        requestSuccessCompleteHandlers.put(getRequestKey(request), completeHandler);
    }

    public void addRequestFailCompleteHandler(Class<? extends ServerRequest> request, DominoTestClient.RequestCompleteHandler completeHandler) {
        requestFailCompleteHandlers.put(getRequestKey(request), completeHandler);
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
            completeSuccessRequest(request);

        }

        private Request.ServerSuccessRequestStateContext makeSuccessContext() {
            return new Request.ServerSuccessRequestStateContext(responseBean);
        }
    }

    private void completeSuccessRequest(ServerRequest request) {
        String requestKey = getRequestKey(request);
        if (requestSuccessCompleteHandlers.containsKey(requestKey)) {
            requestSuccessCompleteHandlers.get(requestKey)
                    .onCompleted();
            requestSuccessCompleteHandlers.remove(requestKey);
        }
    }

    private void completeFailRequest(ServerRequest request) {
        String requestKey = getRequestKey(request);
        if (requestFailCompleteHandlers.containsKey(requestKey)) {
            requestFailCompleteHandlers.get(requestKey)
                    .onCompleted();
            requestFailCompleteHandlers.remove(requestKey);
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
            completeFailRequest(request);

        }

        private Request.ServerFailedRequestStateContext makeFailedContext() {
            return new Request.ServerFailedRequestStateContext(failedResponseBean);
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
        void onRouteRequest(ServerRequest request,
                            Object response);
    }

    public interface ResponseReply {
        ResponseBean reply() throws Exception;
    }

    public static class SuccessReply implements ResponseReply {
        private final ResponseBean response;

        public SuccessReply(ResponseBean response) {
            this.response = response;
        }

        @Override
        public ResponseBean reply() {
            return response;
        }
    }

    public static class FailedReply implements ResponseReply {
        private final FailedResponseBean failedResponseBean;

        public FailedReply(FailedResponseBean failedResponseBean) {
            this.failedResponseBean = failedResponseBean;
        }

        @Override
        public ResponseBean reply() throws Exception {
            throw new FakeRequestFailure(failedResponseBean);
        }
    }

    private static class FakeRequestFailure extends Exception {
        private final FailedResponseBean failedResponseBean;

        public FakeRequestFailure(FailedResponseBean failedResponseBean) {
            this.failedResponseBean = failedResponseBean;
        }
    }
}
