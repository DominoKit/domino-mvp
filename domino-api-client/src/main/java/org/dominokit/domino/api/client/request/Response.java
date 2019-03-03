package org.dominokit.domino.api.client.request;

public interface Response<S> {
    CanFailOrSend onSuccess(Success<S> success);
}
