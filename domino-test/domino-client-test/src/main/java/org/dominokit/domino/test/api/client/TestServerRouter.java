package org.dominokit.domino.test.api.client;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.EventsBus;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.Request;
import org.dominokit.domino.api.client.request.RequestRouter;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.server.entrypoint.ServerEntryPointContext;
import org.dominokit.domino.api.server.handler.HandlersRepository;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.client.commons.request.RequestAsyncSender;
import org.dominokit.domino.gwt.client.request.GwtRequestAsyncSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

public class TestServerRouter implements RequestRouter<ServerRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServerRouter.class);

    private Map<String, ResponseReply> fakeResponses = new HashMap<>();
    private final RequestAsyncSender requestAsyncRunner;
    private TestRoutingListener defaultListener = new TestRoutingListener();
    private RoutingListener listener = defaultListener;

    private final ServerRequestEventFactory eventFactory = new ServerRequestEventFactory() {
        @Override
        public Event makeSuccess(ServerRequest request, ResponseBean responseBean) {
            return new TestServerSuccessEvent(request, responseBean);
        }

        @Override
        public Event makeFailed(ServerRequest request, FailedResponseBean failedResponseBean) {
            return new TestServerFailedEvent(request, failedResponseBean);
        }
    };

    private ServerEntryPointContext entryPointContext;
    private TestContext testContext;
    private Map<String, Async> requestsAsync = new HashMap<>();

    public TestServerRouter(ServerEntryPointContext entryPointContext, TestContext testContext) {
        this.requestAsyncRunner = new GwtRequestAsyncSender(eventFactory);
        this.entryPointContext = entryPointContext;
        this.testContext = testContext;
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
            if (fakeResponses.containsKey(request.getKey())) {
                response = fakeResponses.get(request.getKey()).reply();
                listener.onRouteRequest(request, response);
                eventFactory.makeSuccess(request, response).fire();
            } else {
                if (nonNull(testContext)) {
                    Async async = testContext.async();
                    requestsAsync.put(request.getKey(), async);
                }
                requestAsyncRunner.send(request);
            }
        } catch (HandlersRepository.RequestHandlerNotFound ex) {
            LOGGER.error("Request handler not found for request [" + request.getClass().getSimpleName() + "]! either fake the request or start an actual server");
            eventFactory.makeFailed(request, new FailedResponseBean(ex)).fire();
            return;
        } catch (Exception ex) {
            LOGGER.error("could not execute request : ", ex);
            eventFactory.makeFailed(request, new FailedResponseBean(ex)).fire();
            return;
        }
    }

    public void fakeResponse(String requestKey, ResponseReply reply) {
        fakeResponses.put(requestKey, reply);
    }

    public TestRoutingListener getDefaultRoutingListener() {
        return defaultListener;
    }

    public class TestServerSuccessEvent implements Event {
        protected final ServerRequest request;
        protected final ResponseBean responseBean;
        private final ClientApp clientApp = ClientApp.make();

        public TestServerSuccessEvent(ServerRequest request, ResponseBean responseBean) {
            this.request = request;
            this.responseBean = responseBean;
        }

        @Override
        public void fire() {
            clientApp.getEventsBus().publishEvent(new TestSuccessRequestEvent(this));
        }

        @Override
        public void process() {
            if (nonNull(testContext)) {
                testContext.verify(event -> {
                    request.applyState(new Request.ServerResponseReceivedStateContext(makeSuccessContext()));
                    completeIfAsync(request);
                });
            } else {
                request.applyState(new Request.ServerResponseReceivedStateContext(makeSuccessContext()));
            }
        }

        private Request.ServerSuccessRequestStateContext makeSuccessContext() {
            return new Request.ServerSuccessRequestStateContext(responseBean);
        }
    }

    private void completeIfAsync(ServerRequest request) {
        if (requestsAsync.containsKey(request.getKey())) {
            Async async = requestsAsync.get(request.getKey());
            async.complete();
            requestsAsync.remove(request.getKey());
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
            if (nonNull(testContext)) {
                testContext.verify(event -> {
                    request.applyState(new Request.ServerResponseReceivedStateContext(makeFailedContext()));
                    completeIfAsync(request);
                });
            } else {
                request.applyState(new Request.ServerResponseReceivedStateContext(makeFailedContext()));
            }
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
                            ResponseBean response);
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
        private final Exception error;

        public FailedReply(Exception error) {
            this.error = error;
        }

        @Override
        public ResponseBean reply() throws Exception {
            throw error;
        }
    }
}
