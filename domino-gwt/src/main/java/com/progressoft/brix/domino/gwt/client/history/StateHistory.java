package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.shared.history.AppHistory;
import com.progressoft.brix.domino.api.shared.history.HistoryToken;
import com.progressoft.brix.domino.api.shared.history.TokenFilter;
import com.progressoft.brix.domino.gwt.client.history.History.PopStateEventListener;

import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class StateHistory implements AppHistory {

    private Set<HistoryListener> listeners = new HashSet<>();

    public StateHistory() {
        PopStateEventListener listener =
                event -> {
                    if (nonNull(event.getState()))
                        inform(event.getState().historyToken, event.getState().title, event.getState().data);
                };
        Window.getSelf().addEventListener("popstate", listener::onPopState);
    }

    private void inform(String token, String title, String stateJson) {
        listeners.stream().filter(l -> l.tokenFilter.filter(token))
                .forEach(l -> ClientApp.make().getAsyncRunner().runAsync(() ->
                        l.listener.onPopState(new DominoHistoryState(token, title, stateJson))));
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

    @Override
    public void back() {
        Window.getSelf().getHistory().back();
    }

    @Override
    public void forward() {
        Window.getSelf().getHistory().forward();
    }

    @Override
    public void pushState(String token, String title, String data) {
        if (nonNull(currentToken().value()) && !currentToken().value().equals(token))
            Window.getSelf().getHistory().pushState(History.state(token, title, data), title, "/" + token);
    }

    @Override
    public void replaceState(String token, String title, String data) {
        Window.getSelf().getHistory().replaceState(
                History.state(token, data), title, "/" + token);
    }

    @Override
    public StateHistoryToken currentToken() {
        return new StateHistoryToken(windowToken());
    }

    @Override
    public void fireCurrentStateHistory() {
        fireState(windowToken(), stateData(windowState()));
    }

    private void fireState(String token, String state) {
        replaceState(token, windowTitle(), state);
        inform(token, windowTitle(), state);
    }

    private History.State windowState() {
        return Window.getSelf().getHistory().getState();
    }

    private String windowTitle() {
        return Window.getSelf().getDocument().getTitle();
    }

    private String windowToken() {
        return Window.getSelf().getLocation().getPathname().substring(1) + Window.getSelf().getLocation().getSearch();
    }

    private State currentState() {
        return new DominoHistoryState(windowToken(), windowTitle(), stateData(windowState()));
    }

    private String stateData(History.State state) {
        return isNull(state) ? "" : state.data;
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
