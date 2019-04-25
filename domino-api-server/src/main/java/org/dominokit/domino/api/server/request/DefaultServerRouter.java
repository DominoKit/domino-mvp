package org.dominokit.domino.api.server.request;

import org.dominokit.domino.api.shared.request.*;

public class DefaultServerRouter implements RequestRouter<ServerRequest> {

    @Override
    public void routeRequest(ServerRequest request) {
        request.getSender()
                .send(request, new ServerRequestCallBack() {
                    @Override
                    public void onFailure(FailedResponseBean failedResponse) {
                        request.applyState(new Request.ServerResponseReceivedStateContext(new Request.ServerFailedRequestStateContext(failedResponse)));
                    }

                    @Override
                    public <T> void onSuccess(T response) {
                        request.applyState(new Request.ServerResponseReceivedStateContext(new Request.ServerSuccessRequestStateContext(response)));
                    }
                });
    }
}
