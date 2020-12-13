package org.dominokit.domino.api.client.startup;

public interface PresenterRoutingTask {
    void disable();

    void enable();

    boolean isEnabled();
}
