package org.dominokit.domino.api.client.events;

import org.dominokit.domino.api.shared.extension.ContextAggregator;
import org.dominokit.domino.api.shared.extension.ActivationEvent;
import org.dominokit.domino.api.shared.history.DominoHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class BaseRoutingAggregator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRoutingAggregator.class);

    private final List<Class<? extends ActivationEvent>> events;
    private ContextAggregator.ContextWait<DominoHistory.State> routingEvent = ContextAggregator.ContextWait.create();

    private ContextAggregator contextAggregator;

    protected BaseRoutingAggregator(List<Class<? extends ActivationEvent>> events) {
       this.events = events;
    }

    public void init(Consumer<DominoHistory.State> stateConsumer) {
        contextAggregator = ContextAggregator.waitFor(routingEvent)
                .onReady(() -> {
                    stateConsumer.accept(routingEvent.get());
                    contextAggregator.resetContext(routingEvent);
                });
        events.stream()
                .map(ActivationEventWait::new)
                .forEach(w -> w.setAggregator(contextAggregator));
    }

    public void completeRoutingState(DominoHistory.State state) {
        routingEvent.complete(state);
    }

    public void resetRoutingState() {
        contextAggregator.resetContext(routingEvent);
    }

}
