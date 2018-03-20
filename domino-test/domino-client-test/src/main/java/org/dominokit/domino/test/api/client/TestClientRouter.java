package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.ClientRequestEventFactory;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.EventsBus;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.api.client.request.Request;
import org.dominokit.domino.api.client.request.RequestRouter;


public class TestClientRouter implements RequestRouter<PresenterCommand> {

    private static final ClientRequestEventFactory eventFactory = TestClientEvent::new;

    @Override
    public void routeRequest(PresenterCommand request) {
        eventFactory.make(request).fire();
    }

    public static class TestClientEvent implements Event {
        protected final PresenterCommand request;
        private final ClientApp clientApp = ClientApp.make();

        public TestClientEvent(PresenterCommand request) {
            this.request = request;
        }

        @Override
        public void fire() {
            clientApp.getEventsBus().publishEvent(new TestRequestEvent(this));
        }

        @Override
        public void process() {
            request.applyState(new Request.DefaultRequestStateContext());
        }
    }

    public static class TestRequestEvent implements EventsBus.RequestEvent<TestClientEvent> {

        private final TestClientEvent event;

        public TestRequestEvent(TestClientEvent event) {
            this.event = event;
        }

        @Override
        public TestClientEvent asEvent() {
            return event;
        }
    }


}
