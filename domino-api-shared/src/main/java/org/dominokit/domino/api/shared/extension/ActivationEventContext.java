package org.dominokit.domino.api.shared.extension;

public class ActivationEventContext implements EventContext {
   private final boolean activated;

    public ActivationEventContext(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
    }
}
