package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.shared.history.AppHistory;
import org.dominokit.domino.api.shared.history.HistoryToken;
import org.dominokit.domino.api.shared.history.TokenFilter;
import org.dominokit.domino.client.commons.history.DominoDirectState;
import org.dominokit.domino.client.commons.history.StateHistoryToken;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static java.util.Objects.isNull;

public class TestDominoHistory implements AppHistory {

    private Set<HistoryListener> listeners = new HashSet<>();
    private Deque<HistoryState> forwards = new LinkedList<>();
    private Deque<HistoryState> backwards = new LinkedList<>();

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
        if(forwards.isEmpty())
            return new TestState(nullState());
        return new TestState(forwards.peek());
    }

    private HistoryState nullState() {
        return new HistoryState("", "");
    }

    private void inform(HistoryState state) {
        listeners.stream().filter(l -> l.tokenFilter.filter(new StateHistoryToken(state.token)))
                .forEach(l -> l.listener.onPopState(new TestState(state)));
    }

    @Override
    public void back() {
        if (!backwards.isEmpty()) {
            final HistoryState state = backwards.pop();
            forwards.push(state);
            inform(state);
        }
    }

    @Override
    public void forward() {
        if (!forwards.isEmpty()) {
            final HistoryState state = forwards.pop();
            backwards.push(state);
            inform(state);
        }
    }

    @Override
    public void pushState(String token, String title, String data) {
        push(token, data);
    }

    @Override
    public void pushState(String token) {
        push(token, "");
    }

    @Override
    public void replaceState(String token, String title, String data) {
        forwards.pop();
        push(token, data);
    }

    @Override
    public HistoryToken currentToken() {
        if (isNull(forwards.peek()))
            return new StateHistoryToken("");
        return new StateHistoryToken(forwards.peek().token);
    }

    @Override
    public void fireCurrentStateHistory() {
        if (!forwards.isEmpty())
            inform(forwards.peek());
    }

    public void initialState(String token, String data) {
        push(token, data);
    }

    private void push(String token, String data) {
        forwards.push(new HistoryState(token, data));
    }

    private class HistoryListener {
        private final StateListener listener;
        private final TokenFilter tokenFilter;

        public HistoryListener(StateListener listener, TokenFilter tokenFilter) {
            this.listener = listener;
            this.tokenFilter = tokenFilter;
        }
    }

    public Set<HistoryListener> getListeners() {
        return listeners;
    }

    public Deque<HistoryState> getForwards() {
        return forwards;
    }

    public Deque<HistoryState> getBackwards() {
        return backwards;
    }

    private class TestState implements State {

        private final HistoryState historyState;

        private TestState(HistoryState historyState) {
            this.historyState = historyState;
        }

        @Override
        public HistoryToken token() {
            return new StateHistoryToken(historyState.token);
        }

        @Override
        public String data() {
            return historyState.data;
        }

        @Override
        public String title() {
            return "test title";
        }
    }

    public class HistoryState {
        private final String token;
        private final String data;

        public HistoryState(String token, String data) {
            this.token = token;
            this.data = data;
        }

        public String getToken() {
            return token;
        }

        public String getData() {
            return data;
        }
    }
}
