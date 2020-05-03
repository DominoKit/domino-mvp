package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.extension.*;

public class ActivationEventWait {

    private ContextAggregator.ContextWait<Boolean> contextWait = ContextAggregator.ContextWait.create();
    private ContextAggregator aggregator;
    private Class<? extends ActivationEvent> eventType;

    public ActivationEventWait(Class<? extends ActivationEvent> eventType) {
        this.eventType = eventType;
        ClientApp.make().registerGlobalEventListener(eventType, new ActivateEventListener() {
            @Override
            public void onEventReceived(ActivationEvent dominoEvent) {
                updateContext(dominoEvent);
            }
        });
    }

    public void setAggregator(ContextAggregator aggregator) {
        this.aggregator = aggregator;
        aggregator.setupContext(contextWait);
    }

    protected void updateContext(DominoEvent event) {
        ActivationEventContext eventContext = (ActivationEventContext) event.context();
        if (eventContext.isActivated()) {
            contextWait.complete(true);
        } else {
            aggregator.resetContext(contextWait);
        }
    }

    private static abstract class ActivateEventListener implements GlobalDominoEventListener<ActivationEvent>{

        @Override
        public ActivationEvent deserializeEvent(String serializedEvent) {
            return new ActivationEventImpl(serializedEvent);
        }
    }

    private static class ActivationEventImpl extends ActivationEvent {

        public ActivationEventImpl(boolean active) {
            super(active);
        }

        public ActivationEventImpl(String serializedEvent) {
            super(serializedEvent);
        }
    }
}
