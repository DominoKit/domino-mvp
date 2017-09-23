package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.events.EventsBus;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.api.client.request.RequestRouter;
import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TestServerRouter implements RequestRouter<ClientServerRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServerRouter.class);

    private Map<String, ResponseReply> fakeResponses=new HashMap<>();

    private TestRoutingListener defaultListener = new TestRoutingListener();
    private RoutingListener listener = defaultListener;

    private final ServerRequestEventFactory eventFactory = new ServerRequestEventFactory() {
        @Override
        public Event makeSuccess(ClientServerRequest request, ServerResponse serverResponse) {
            return new TestServerSuccessEvent(request, serverResponse);
        }

        @Override
        public Event makeFailed(ClientServerRequest request, Throwable error) {
            return new TestServerFailedEvent(request, error);
        }
    };

    private ServerEntryPointContext entryPointContext;

    private final TestServerService service = request -> ServerApp.make().executeRequest(request, entryPointContext);

    public TestServerRouter(ServerEntryPointContext entryPointContext) {
        this.entryPointContext = entryPointContext;
    }

    public void setRoutingListener(RoutingListener listener) {
        this.listener = listener;
    }

    public void removeRoutingListener() {
        this.listener = defaultListener;
    }

    @Override
    public void routeRequest(ClientServerRequest request) {
        ServerResponse response;
        try {
            if(fakeResponses.containsKey(request.getKey())){
                response=fakeResponses.get(request.getKey()).reply();
            }else {
                response = service.executeRequest(request.arguments());
            }
            listener.onRouteRequest(request, response);
        } catch (Throwable ex) {
            LOGGER.error("could not execute request : ", ex);
            eventFactory.makeFailed(request, ex).fire();
            return;
        }
        eventFactory.makeSuccess(request, response).fire();
    }

    public void fakeResponse(String requestKey, ResponseReply reply){
        fakeResponses.put(requestKey, reply);
    }

    public TestRoutingListener getDefaultRoutingListener() {
        return defaultListener;
    }

    public class TestServerSuccessEvent implements Event {
        protected final ClientServerRequest request;
        protected final ServerResponse serverResponse;
        private final ClientApp clientApp = ClientApp.make();

        public TestServerSuccessEvent(ClientServerRequest request, ServerResponse serverResponse) {
            this.request = request;
            this.serverResponse = serverResponse;
        }

        @Override
        public void fire() {
            clientApp.getEventsBus().publishEvent(new TestSuccessRequestEvent(this));
        }

        @Override
        public void process() {
            request.applyState(new Request.ServerResponseReceivedStateContext(makeSuccessContext()));
        }

        private Request.ServerSuccessRequestStateContext makeSuccessContext() {
            return new Request.ServerSuccessRequestStateContext(serverResponse);
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
        protected final ClientServerRequest request;
        protected final Throwable error;
        private final ClientApp clientApp = ClientApp.make();

        public TestServerFailedEvent(ClientServerRequest request, Throwable error) {
            this.request = request;
            this.error = error;
        }

        @Override
        public void fire() {
            clientApp.getEventsBus().publishEvent(new TestFailedRequestEvent(this));
        }

        @Override
        public void process() {
            request.applyState(new Request.ServerResponseReceivedStateContext(makeFailedContext()));
        }

        private Request.ServerFailedRequestStateContext makeFailedContext() {
            return new Request.ServerFailedRequestStateContext(new FailedServerResponse(error));
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
        void onRouteRequest(ClientServerRequest request,
                            ServerResponse response);
    }

    public interface ResponseReply{
        ServerResponse reply() throws Throwable;
    }

    public static class SuccessReply implements ResponseReply{
        private final ServerResponse response;

        public SuccessReply(ServerResponse response) {
            this.response = response;
        }

        @Override
        public ServerResponse reply() {
            return response;
        }
    }

    public static class FailedReply implements ResponseReply{
        private final Throwable error;

        public FailedReply(Throwable error) {
            this.error = error;
        }

        @Override
        public ServerResponse reply() throws Throwable {
            throw error;
        }
    }
}
