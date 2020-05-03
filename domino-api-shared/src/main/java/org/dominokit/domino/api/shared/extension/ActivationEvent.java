package org.dominokit.domino.api.shared.extension;

public abstract class ActivationEvent extends GlobalEvent<ActivationEventContext> {

    private final ActivationEventContext context;

    public ActivationEvent(boolean active) {
        this.context = new ActivationEventContext(active);
    }

    public ActivationEvent(String serializedEvent) {
        this.context = new ActivationEventContext(Boolean.parseBoolean(serializedEvent));
    }

    @Override
    public String serialize() {
        return Boolean.toString(context.isActivated());
    }

    @Override
    public ActivationEventContext context() {
        return context;
    }
}
