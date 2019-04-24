package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.shared.extension.ContextAggregator;
import org.dominokit.domino.api.shared.extension.ActivationEvent;
import org.dominokit.domino.api.shared.extension.ActivationEventContext;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivationEventWait {
    private final static Logger LOGGER = LoggerFactory.getLogger(ActivationEventWait.class);

    private ContextAggregator.ContextWait<Boolean> contextWait = ContextAggregator.ContextWait.create();
    private ContextAggregator aggregator;
    private Class<? extends ActivationEvent> eventType;

    public ActivationEventWait(Class<? extends ActivationEvent> eventType) {
        this.eventType = eventType;
        ClientApp.make().registerEventListener(eventType, this::updateContext);
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
}
