package org.dominokit.domino.desktop.client;

import org.dominokit.domino.desktop.client.events.DesktopFailedServerEvent;
import org.dominokit.domino.desktop.client.events.DesktopSuccessServerEvent;
import org.dominokit.domino.rest.shared.Event;
import org.dominokit.domino.rest.shared.request.FailedResponseBean;
import org.dominokit.domino.rest.shared.request.ServerRequest;
import org.dominokit.domino.rest.shared.request.ServerRequestEventFactory;

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
