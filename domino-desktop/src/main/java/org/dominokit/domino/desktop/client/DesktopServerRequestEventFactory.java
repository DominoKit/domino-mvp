package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.FailedResponseBean;
import org.dominokit.domino.desktop.client.events.DesktopFailedServerEvent;
import org.dominokit.domino.desktop.client.events.DesktopSuccessServerEvent;

public class DesktopServerRequestEventFactory implements ServerRequestEventFactory {
    @Override
    public <T> Event makeSuccess(ServerRequest request, T responseBean) {
        return new DesktopSuccessServerEvent(request, responseBean);
    }

    @Override
    public Event makeFailed(ServerRequest request, FailedResponseBean failedResponseBean) {
        return new DesktopFailedServerEvent(request, failedResponseBean);
    }
}
