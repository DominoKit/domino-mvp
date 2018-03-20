package org.dominokit.domino.api.client.request;

import java.util.Objects;

public abstract class LazyRequestRestSenderLoader {

    private RequestRestSender sender;

    public RequestRestSender get(){
        if(Objects.isNull(sender))
            this.sender=make();
        return sender;
    }

    protected abstract RequestRestSender make();
}
