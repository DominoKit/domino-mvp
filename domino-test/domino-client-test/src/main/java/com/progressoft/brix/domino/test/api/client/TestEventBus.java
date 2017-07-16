package com.progressoft.brix.domino.test.api.client;

import com.progressoft.brix.domino.api.client.events.Event;
import com.progressoft.brix.domino.api.client.events.EventProcessor;
import com.progressoft.brix.domino.api.client.events.EventsBus;

public class TestEventBus  implements EventsBus<Event>{

    private final EventProcessor processor;

    public TestEventBus(EventProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void publishEvent(RequestEvent<Event> event) {
        processor.process(event.asEvent());
    }
}
