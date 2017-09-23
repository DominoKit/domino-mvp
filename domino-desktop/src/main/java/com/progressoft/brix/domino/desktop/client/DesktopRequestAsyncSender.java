package com.progressoft.brix.domino.desktop.client;

import com.progressoft.brix.domino.api.client.events.ServerRequestEventFactory;
import com.progressoft.brix.domino.client.commons.request.AbstractRequestAsyncSender;

public class DesktopRequestAsyncSender extends AbstractRequestAsyncSender {

    public DesktopRequestAsyncSender(ServerRequestEventFactory requestEventFactory) {
        super(requestEventFactory);
    }
}
