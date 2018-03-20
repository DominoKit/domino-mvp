package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.events.ServerRequestEventFactory;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.desktop.client.events.DesktopFailedServerEvent;
import org.dominokit.domino.desktop.client.events.DesktopSuccessServerEvent;

public class DesktopServerRequestEventFactory implements ServerRequestEventFactory {
    @Override
    public Event makeSuccess(ServerRequest request, ResponseBean responseBean) {
        return new DesktopSuccessServerEvent(request, responseBean);
    }

    @Override
    public Event makeFailed(ServerRequest request, Throwable error) {
        return new DesktopFailedServerEvent(request, error);
    }
}
