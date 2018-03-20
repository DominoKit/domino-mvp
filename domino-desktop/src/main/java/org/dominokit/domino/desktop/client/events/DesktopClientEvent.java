package org.dominokit.domino.desktop.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.Event;
import org.dominokit.domino.api.client.request.PresenterCommand;
import org.dominokit.domino.api.client.request.Request;

public class DesktopClientEvent implements Event {

    private final PresenterCommand request;

    public DesktopClientEvent(PresenterCommand request) {
        this.request = request;
    }

    @Override
    public void fire() {
        ClientApp.make().getEventsBus().publishEvent(() -> this);
    }

    @Override
    public void process() {
        request.applyState(new Request.DefaultRequestStateContext());
    }
}
