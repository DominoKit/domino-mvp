package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.shared.request.ResponseBean;

@FunctionalInterface
public interface Success<S extends ResponseBean> {
    void onSuccess(S response);
}
