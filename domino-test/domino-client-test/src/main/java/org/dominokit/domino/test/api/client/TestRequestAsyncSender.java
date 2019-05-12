package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ServerRequest;
import org.dominokit.domino.api.shared.request.ServerRequestCallBack;
import org.dominokit.domino.client.commons.request.AbstractRequestAsyncSender;

public class TestRequestAsyncSender extends AbstractRequestAsyncSender {

    public TestRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void sendRequest(ServerRequest request, ServerRequestEventFactory requestEventFactory) {

        request.getSender()
                .send(request,
                        new ServerRequestCallBack() {

                            @Override
                            public <T> void onSuccess(T response) {
                                requestEventFactory.makeSuccess(request, response).fire();
                            }

                            @Override
                            public void onFailure(FailedResponseBean failedResponse) {
                                requestEventFactory.makeFailed(request, failedResponse).fire();
                            }
                        });

    }
}
