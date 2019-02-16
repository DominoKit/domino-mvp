package org.dominokit.domino.gwt.client.request;

import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.client.commons.request.AbstractRequestAsyncSender;

public class GwtRequestAsyncSender extends AbstractRequestAsyncSender {

    public GwtRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void sendRequest(ServerRequest request, ServerRequestEventFactory requestEventFactory) {
//        request.headers().put("X-XSRF-TOKEN", Cookies.getCookie("XSRF-TOKEN"));
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
