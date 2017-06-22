package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.shared.history.AppHistory;
import com.progressoft.brix.domino.api.shared.history.StateToken;
import com.progressoft.brix.domino.api.shared.history.TokenFilter;
import com.progressoft.brix.domino.gwt.client.history.History.PopStateEventListener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class StateHistory implements AppHistory {

    private Set<HistoryListener> listeners = new HashSet<>();

    public StateHistory() {
        PopStateEventListener listener =
                event -> inform(event.getState().historyToken, event.getState().data);
        Window.getSelf().addEventListener("popstate", listener::onPopState);
    }

    private void inform(String token, String stateJson) {
        listeners.stream().filter(l -> l.tokenFilter.filter(token)).forEach(l -> {
            ClientApp.make().getAsyncRunner().runAsync(() ->
                    l.listener.onPopState(token, stateJson));
        });
    }

    @Override
    public void listen(StateListener listener) {
        listeners.add(new HistoryListener(listener, TokenFilter.any()));
    }

    @Override
    public void listen(StateListener listener, TokenFilter tokenFilter) {
        listeners.add(new HistoryListener(listener, tokenFilter));
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
        Window.getSelf().getHistory().pushState(History.state(token, data), title, "/" + token);
    }

    @Override
    public void replaceState(String token, String title, String data) {
        Window.getSelf().getHistory().replaceState(
                History.state(token, data), title, "/" + token);
    }

    @Override
    public StateToken currentState() {
        return null;
    }

    @Override
    public void fireCurrentStateHistory() {
        String token =
                Window.getSelf().getLocation().getPathname().substring(1) + Window.getSelf().getLocation().getSearch();
        History.State state = Window.getSelf().getHistory().getState();
        replaceState(token, Window.getSelf().getDocument().getTitle(),
                stateData(state));
        inform(token, stateData(state));
    }

    private String stateData(History.State state) {
        return Objects.isNull(state) ? "" : state.data;
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
}
