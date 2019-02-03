package org.dominokit.domino.api.client.startup;

import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.api.client.events.BaseRoutingAggregator;
import org.dominokit.domino.api.client.mvp.presenter.BaseClientPresenter;
import org.dominokit.domino.api.shared.history.DominoHistory;
import org.dominokit.domino.api.shared.history.TokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public abstract class BaseRoutingStartupTask implements ClientStartupTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRoutingStartupTask.class);

    private List<BaseRoutingAggregator> aggregators = new ArrayList<>();
    private boolean enabled = true;
    private BaseClientPresenter presenter;

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

    protected void bindPresenter(BaseClientPresenter presenter){
        presenter.setRoutingTask(this);
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        ClientApp.make()
                .getHistory()
                .listen(getTokenFilter(), state -> {
                    if(enabled) {
                        aggregators.forEach(aggregator -> aggregator.completeRoutingState(state));
                    }else{
                        if(nonNull(presenter)){
                            presenter.onSkippedRouting();
                        }
                    }

                }, isRoutingOnce())
                .onDirectUrl(getStartupTokenFilter());
    }

    protected abstract void onStateReady(DominoHistory.State state);

    protected abstract TokenFilter getTokenFilter();

    protected TokenFilter getStartupTokenFilter() {
        return getTokenFilter();
    }

    protected boolean isRoutingOnce() {
        return false;
    }

    public void disable(){
        this.enabled = false;
    }

    public void enable(){
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
