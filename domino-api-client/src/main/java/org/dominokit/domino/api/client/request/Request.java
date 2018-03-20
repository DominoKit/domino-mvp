package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

public interface Request {

    class DefaultRequestStateContext implements RequestStateContext{
    }

    class ServerResponseReceivedStateContext implements  RequestStateContext{
        protected final RequestStateContext nextContext;

        public ServerResponseReceivedStateContext(RequestStateContext nextContext) {
            this.nextContext = nextContext;
        }
    }

    class ServerSuccessRequestStateContext implements RequestStateContext{

        protected final ResponseBean responseBean;

        public ServerSuccessRequestStateContext(ResponseBean responseBean) {
            this.responseBean = responseBean;
        }
    }

    class ServerFailedRequestStateContext implements RequestStateContext{

        protected final FailedResponseBean response;

        public ServerFailedRequestStateContext(FailedResponseBean response) {
            this.response=response;
        }
    }

    void startRouting();

    String getKey();

    void applyState(RequestStateContext context);

    class InvalidRequestState extends RuntimeException{

        private static final long serialVersionUID = 1976356149064117774L;

        public InvalidRequestState(String message) {
            super(message);
        }
    }
}
