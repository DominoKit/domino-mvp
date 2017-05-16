package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.events.EventsBus;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.api.client.request.RequestRouter;
import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.ServerEntryPointContext;
import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;

public class TestServerRouter implements RequestRouter<ClientServerRequest> {

    private static final CoreLogger LOGGER = CoreLoggerFactory.getLogger(TestServerRouter.class);

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

    @Override
    public void routeRequest(ClientServerRequest request) {
        ServerResponse response;
        try {
            response = service.executeRequest(request.arguments());
        } catch (Exception ex) {
            LOGGER.error("could not execute request : ", ex);
            eventFactory.makeFailed(request, ex).fire();
            return;
        }
        eventFactory.makeSuccess(request, response).fire();
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
            request.applyState(new Request.ServerResponseRecievedStateContext(makeSuccessContext()));
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
            request.applyState(new Request.ServerResponseRecievedStateContext(makeFailedContext()));
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
}
