package org.dominokit.domino.api.shared.extension;

public abstract class ActivationEvent implements DominoEvent<ActivationEventContext> {

    private final ActivationEventContext context;

    public ActivationEvent(ActivationEventContext context) {
        this.context = context;
    }

    @Override
    public ActivationEventContext context() {
        return context;
    }
}
