package org.dominokit.domino.api.shared.request;

public interface Response<S> {
    CanFailOrSend onSuccess(Success<S> success);
}
