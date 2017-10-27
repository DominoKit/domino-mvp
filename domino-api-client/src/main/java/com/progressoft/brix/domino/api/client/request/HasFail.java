package com.progressoft.brix.domino.api.client.request;

public interface HasFail {
    CanSend onFailed(Fail fail);
}
