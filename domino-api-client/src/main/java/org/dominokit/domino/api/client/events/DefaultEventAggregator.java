package org.dominokit.domino.api.client.events;

import java.util.ArrayList;

public class DefaultEventAggregator extends BaseRoutingAggregator {

    public DefaultEventAggregator() {
        super(new ArrayList<>());
    }
}
