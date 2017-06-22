package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.shared.history.AppHistory;
import com.progressoft.brix.domino.api.shared.history.StateToken;
import com.progressoft.brix.domino.api.shared.history.TokenFilter;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class TestDominoHistory implements AppHistory {

    private Set<HistoryListener> listeners = new HashSet<>();
    private Deque<HistoryState> forwards = new LinkedList<>();
    private Deque<HistoryState> backwards = new LinkedList<>();

    @Override
    public void listen(StateListener listener) {
        listeners.add(new HistoryListener(listener, TokenFilter.any()));
    }

    @Override
    public void listen(StateListener listener, TokenFilter tokenFilter) {
        listeners.add(new HistoryListener(listener, tokenFilter));
    }

    private void inform(HistoryState state) {
        listeners.stream().filter(l -> l.tokenFilter.filter(state.token))
                .forEach(l -> l.listener.onPopState(state.token, state.data));
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
    public void replaceState(String token, String title, String data) {
        forwards.pop();
        push(token, data);
    }

    @Override
    public StateToken currentState() {
        return null;
    }

    @Override
    public void fireCurrentStateHistory() {
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

    private class HistoryState {
        private final String token;
        private final String data;

        public HistoryState(String token, String data) {
            this.token = token;
            this.data = data;
        }
    }
}
