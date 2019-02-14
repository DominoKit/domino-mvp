package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.ResponseBean;

public interface Response<S> {
    CanFailOrSend onSuccess(Success<S> success);
}
