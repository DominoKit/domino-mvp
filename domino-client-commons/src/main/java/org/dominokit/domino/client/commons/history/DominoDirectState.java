package org.dominokit.domino.client.commons.history;

import org.dominokit.domino.api.shared.history.TokenFilter;

import static org.dominokit.domino.api.shared.history.DominoHistory.*;

public class DominoDirectState implements DirectState {

    private final TokenFilter tokenFilter;
    private final State state;

    public DominoDirectState(TokenFilter tokenFilter, State state) {
        this.tokenFilter = tokenFilter;
        this.state = state;
    }

    @Override
    public void onDirectUrl(DirectUrlHandler handler) {
        if (tokenFilter.filter(state.token()))
            handler.handle(state);
    }
}