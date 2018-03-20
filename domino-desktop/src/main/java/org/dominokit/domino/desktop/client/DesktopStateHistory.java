package org.dominokit.domino.desktop.client;

import org.dominokit.domino.api.shared.history.AppHistory;
import org.dominokit.domino.api.shared.history.HistoryToken;
import org.dominokit.domino.api.shared.history.TokenFilter;
import org.dominokit.domino.client.commons.history.DominoDirectState;
import org.dominokit.domino.client.commons.history.StateHistoryToken;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

public class DesktopStateHistory implements AppHistory {

    private Set<HistoryListener> listeners = new HashSet<>();

    @Override
    public void fireCurrentStateHistory() {
        //not implemented for desktop
    }

    @Override
    public DirectState listen(StateListener listener) {
        return listen(TokenFilter.any(), listener);
    }

    @Override
    public DirectState listen(TokenFilter tokenFilter, StateListener listener) {
        listeners.add(new HistoryListener(listener, tokenFilter));
        return new DominoDirectState(tokenFilter, currentState());
    }

    private State currentState() {
        return new DominoHistoryState("", "", stateData(state()));
    }

    private State state() {
        return new State() {
            @Override
            public HistoryToken token() {
                return new StateHistoryToken("");
            }

            @Override
            public String data() {
                return "";
            }

            @Override
            public String title() {
                return "";
            }
        };
    }

    private String stateData(State state) {
        return isNull(state) ? "" : state.data();
    }

    @Override
    public void back() {
        //not implemented for desktop
    }

    @Override
    public void forward() {
        //not implemented for desktop
    }

    @Override
    public void pushState(String token, String title, String data) {
        //not implemented for desktop
    }

    @Override
    public void pushState(String token) {
        //not implemented for desktop
    }

    @Override
    public void replaceState(String token, String title, String data) {
        //not implemented for desktop
    }

    @Override
    public HistoryToken currentToken() {
        return new StateHistoryToken("");
    }

    private class HistoryListener {
        private final StateListener listener;

        private final TokenFilter tokenFilter;

        private HistoryListener(StateListener listener,
                                TokenFilter tokenFilter) {
            this.listener = listener;
            this.tokenFilter = tokenFilter;
        }
    }

    private class DominoHistoryState implements State {

        private final HistoryToken token;
        private final String data;
        private final String title;

        public DominoHistoryState(String token, String title, String data) {
            this.token = new StateHistoryToken(token);
            this.data = data;
            this.title = title;
        }

        @Override
        public HistoryToken token() {
            return this.token;
        }

        @Override
        public String data() {
            return this.data;
        }

        @Override
        public String title() {
            return this.title;
        }
    }
}
