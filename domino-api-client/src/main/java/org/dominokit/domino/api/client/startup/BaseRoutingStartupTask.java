package org.dominokit.domino.api.client.startup;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.BaseRoutingAggregator;
import org.dominokit.domino.api.client.startup.ClientStartupTask;
import org.dominokit.domino.api.shared.history.DominoHistory;
import org.dominokit.domino.api.shared.history.TokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRoutingStartupTask implements ClientStartupTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRoutingStartupTask.class);

    private List<BaseRoutingAggregator> aggregators = new ArrayList<>();

    public BaseRoutingStartupTask(List<? extends BaseRoutingAggregator> aggregators) {
        this.aggregators.addAll(aggregators);
        aggregators.forEach(aggregator -> aggregator.init(state -> {
            onStateReady(state);
            resetRouting();
        }));
    }

    private void resetRouting() {
        aggregators.forEach(BaseRoutingAggregator::resetRoutingState);
    }

    @Override
    public void execute() {
        ClientApp.make().getHistory().listen(getTokenFilter(), state -> {
            aggregators.forEach(aggregator -> aggregator.completeRoutingState(state));
        }, isRoutingOnce());
    }

    protected abstract void onStateReady(DominoHistory.State state);
    protected abstract TokenFilter getTokenFilter();
    protected boolean isRoutingOnce(){
        return false;
    }
}
