package org.dominokit.domino.api.client.request;

public interface HasFail {
    CanSend onFailed(Fail fail);
}
