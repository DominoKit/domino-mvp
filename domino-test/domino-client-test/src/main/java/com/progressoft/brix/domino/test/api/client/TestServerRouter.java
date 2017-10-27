package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.events.EventsBus;
import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.client.request.Request;
import com.progressoft.brix.domino.api.client.request.RequestRouter;
import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.ServerEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.HandlersRepository;
import com.progressoft.brix.domino.api.shared.request.FailedResponseBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TestServerRouter implements RequestRouter<ServerRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestServerRouter.class);

    private Map<String, ResponseReply> fakeResponses=new HashMap<>();

    private TestRoutingListener defaultListener = new TestRoutingListener();
    private RoutingListener listener = defaultListener;

    private final ServerRequestEventFactory eventFactory = new ServerRequestEventFactory() {
        @Override
        public Event makeSuccess(ServerRequest request, ResponseBean responseBean) {
            return new TestServerSuccessEvent(request, responseBean);
        }

        @Override
        public Event makeFailed(ServerRequest request, Throwable error) {
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
    public void routeRequest(ServerRequest request) {
        ResponseBean response;
        try {
            if(fakeResponses.containsKey(request.getKey())){
                response=fakeResponses.get(request.getKey()).reply();
            }else {
                response = service.executeRequest(request.requestBean());
            }
            listener.onRouteRequest(request, response);
        }catch (HandlersRepository.RequestHandlerNotFound ex){
            LOGGER.error("Request handler not found for request ["+request.getClass().getSimpleName()+"]! either fake the request or start an actual server");
            eventFactory.makeFailed(request, ex).fire();
            return;
        }catch (Exception ex) {
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
            request.applyState(new Request.ServerResponseReceivedStateContext(makeSuccessContext()));
        }

        private Request.ServerSuccessRequestStateContext makeSuccessContext() {
            return new Request.ServerSuccessRequestStateContext(responseBean);
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
        protected final Throwable error;
        private final ClientApp clientApp = ClientApp.make();

        public TestServerFailedEvent(ServerRequest request, Throwable error) {
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
            return new Request.ServerFailedRequestStateContext(new FailedResponseBean(error));
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

    public interface ResponseReply{
        ResponseBean reply() throws Exception;
    }

    public static class SuccessReply implements ResponseReply{
        private final ResponseBean response;

        public SuccessReply(ResponseBean response) {
            this.response = response;
        }

        @Override
        public ResponseBean reply() {
            return response;
        }
    }

    public static class FailedReply implements ResponseReply{
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
