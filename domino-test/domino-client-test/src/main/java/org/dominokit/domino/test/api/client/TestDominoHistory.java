package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.shared.history.*;
import org.dominokit.domino.client.commons.history.DominoDirectState;

import java.util.*;

import static java.util.Objects.isNull;

public class TestDominoHistory implements AppHistory {

    private Set<HistoryListener> listeners = new HashSet<>();
    private Deque<HistoryState> forwards = new LinkedList<>();
    private Deque<HistoryState> backwards = new LinkedList<>();

    @Override
    public DirectState listen(StateListener listener) {
        return listen(TokenFilter.any(), listener, false);
    }

    @Override
    public DirectState listen(TokenFilter tokenFilter, StateListener listener) {
        return listen(tokenFilter, listener, false);
    }

    @Override
    public DirectState listen(StateListener listener, boolean removeOnComplete) {
        return listen(TokenFilter.any(), listener, removeOnComplete);
    }

    @Override
    public DirectState listen(TokenFilter tokenFilter, StateListener listener, boolean removeOnComplete) {
        listeners.add(new HistoryListener(listener, tokenFilter, removeOnComplete));
        return new DominoDirectState(tokenFilter, currentState(), listener);
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
        push(token, data, new TokenParameter[0]);
    }

    @Override
    public void pushState(String token, String title, String data, TokenParameter... parameters) {
        push(token, data, parameters);
    }

    @Override
    public void pushState(String token) {
        push(token, "", new TokenParameter[0]);
    }

    @Override
    public void pushState(String token, TokenParameter... parameters) {
        push(token, "", parameters);
    }

    @Override
    public void fireState(String token, String title, String data) {
        fireState(token, title, data, new TokenParameter[0]);
    }

    @Override
    public void fireState(String token, String title, String data, TokenParameter... parameters) {
        pushState(token, title, data, parameters);
        fireCurrentStateHistory();
    }

    @Override
    public void fireState(String token) {
        fireState(token, new TokenParameter[0]);
    }

    @Override
    public void fireState(String token, TokenParameter... parameters) {
        pushState(token, parameters);
        fireCurrentStateHistory();
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

    private void push(String token, String data, TokenParameter... parameters) {

        forwards.push(new HistoryState(replaceParameters(token, Arrays.asList(parameters)), data));
    }

    private String replaceParameters(String token, List<TokenParameter> parametersList) {
        String result = token;
        for (TokenParameter parameter:parametersList) {
            result=result.replace(":"+parameter.getName(), parameter.getValue());
        }
        return result;
    }

    private class HistoryListener {
        private final StateListener listener;
        private final TokenFilter tokenFilter;
        private final boolean removeOnComplete;

        public HistoryListener(StateListener listener, TokenFilter tokenFilter) {
            this.listener = listener;
            this.tokenFilter = tokenFilter;
            this.removeOnComplete = false;
        }

        public HistoryListener(StateListener listener, TokenFilter tokenFilter, boolean removeOnComplete) {
            this.listener = listener;
            this.tokenFilter = tokenFilter;
            this.removeOnComplete = removeOnComplete;
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

        @Override
        public NormalizedToken normalizedToken() {
            return new DefaultNormalizedToken(new StateHistoryToken(historyState.token));
        }

        @Override
        public void setNormalizedToken(NormalizedToken normalizedToken) {

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
