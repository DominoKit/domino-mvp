package org.dominokit.domino.gwt.client.history;

import org.dominokit.domino.api.shared.history.DominoHistory;
import org.dominokit.domino.api.shared.history.TokenFilter;

public class HistoryListener {
    private final DominoHistory.StateListener listener;

    private final TokenFilter tokenFilter;

    private final boolean removeOnComplete;

    private HistoryListener(DominoHistory.StateListener listener,
                            TokenFilter tokenFilter) {
        this.listener = listener;
        this.tokenFilter = tokenFilter;
        this.removeOnComplete = false;
    }

    HistoryListener(DominoHistory.StateListener listener,
                    TokenFilter tokenFilter, boolean removeOnComplete) {
        this.listener = listener;
        this.tokenFilter = tokenFilter;
        this.removeOnComplete = removeOnComplete;
    }

    public DominoHistory.StateListener getListener() {
        return listener;
    }

    public TokenFilter getTokenFilter() {
        return tokenFilter;
    }

    public boolean isRemoveOnComplete() {
        return removeOnComplete;
    }
}
