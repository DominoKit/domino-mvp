package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.shared.history.DominoHistory;
import com.progressoft.brix.domino.api.shared.history.TokenFilter;

public class DominoDirectState implements DominoHistory.DirectState {

    private final TokenFilter tokenFilter;
    private final DominoHistory.State state;

    public DominoDirectState(TokenFilter tokenFilter, DominoHistory.State state) {
        this.tokenFilter = tokenFilter;
        this.state = state;
    }

    @Override
    public void onDirectUrl(DominoHistory.DirectUrlHandler handler) {
        if (tokenFilter.filter(state.token().value()))
            handler.handle(state);
    }
}