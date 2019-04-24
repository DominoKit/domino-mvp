package org.dominokit.domino.api.client.startup;

import org.dominokit.domino.api.shared.extension.ContextAggregator;

public abstract class AsyncClientStartupTask<T> extends ContextAggregator.ContextWait<T> implements ClientStartupTask {

    public abstract int order();
}
