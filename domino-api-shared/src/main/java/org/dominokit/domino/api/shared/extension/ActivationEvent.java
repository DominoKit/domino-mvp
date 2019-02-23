package org.dominokit.domino.api.shared.extension;

public abstract class ActivationEvent implements DominoEvent<ActivationEventContext> {

    private final ActivationEventContext context;

    public ActivationEvent(boolean active) {
        this.context = new ActivationEventContext(active);
    }

    @Override
    public ActivationEventContext context() {
        return context;
    }
}
