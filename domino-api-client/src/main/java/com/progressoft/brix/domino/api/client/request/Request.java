package com.progressoft.brix.domino.api.client.request;

import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;

public interface Request {

    class DefaultRequestStateContext implements RequestStateContext{

    }

    class ServerResponseRecievedStateContext implements  RequestStateContext{
        protected final RequestStateContext nextContext;

        public ServerResponseRecievedStateContext(RequestStateContext nextContext) {
            this.nextContext = nextContext;
        }
    }

    class ServerSuccessRequestStateContext implements RequestStateContext{

        protected final ServerResponse serverResponse;

        public ServerSuccessRequestStateContext(ServerResponse serverResponse) {
            this.serverResponse = serverResponse;
        }
    }

    class ServerFailedRequestStateContext implements RequestStateContext{

        protected final FailedServerResponse response;

        public ServerFailedRequestStateContext(FailedServerResponse response) {
            this.response=response;
        }
    }

    void send();

    void startRouting();

    String getKey();

    void chainRequest(Request request);

    void applyState(RequestStateContext context);

    class InvalidRequestState extends RuntimeException{

        private static final long serialVersionUID = 1976356149064117774L;

        public InvalidRequestState(String message) {
            super(message);
        }
    }
}
