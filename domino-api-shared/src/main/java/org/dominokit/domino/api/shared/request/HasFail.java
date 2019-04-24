package org.dominokit.domino.api.shared.request;

public interface HasFail {
    CanSend onFailed(Fail fail);
}
