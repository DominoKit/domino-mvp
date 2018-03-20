package org.dominokit.domino.gwt.client.request;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.client.commons.request.AbstractRequestAsyncSender;
import org.fusesource.restygwt.client.Defaults;

public class GwtRequestAsyncSender extends AbstractRequestAsyncSender {

    public GwtRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
        Defaults.setDispatcher(new DominoRequestDispatcher());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void sendRequest(ServerRequest request, ServerRequestEventFactory requestEventFactory) {
        ClientApp.make().getRequestRestSendersRepository().get(request.getKey())
                .send(request.requestBean(), request.headers(),
                        new ServerRequestCallBack() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                requestEventFactory.makeFailed(request, throwable).fire();
                            }

                            @Override
                            public void onSuccess(ResponseBean response) {
                                requestEventFactory.makeSuccess(request, response).fire();
                            }
                        });
    }
}
