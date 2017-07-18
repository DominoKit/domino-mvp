package com.progressoft.brix.domino.gwt.client.history;

import com.progressoft.brix.domino.api.client.ClientApp;
import com.progressoft.brix.domino.api.shared.history.AppHistory;
import com.progressoft.brix.domino.api.shared.history.HistoryToken;
import com.progressoft.brix.domino.api.shared.history.TokenFilter;
import com.progressoft.brix.domino.gwt.client.history.History.PopStateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class StateHistory implements AppHistory {

    private static final Logger LOGGER= LoggerFactory.getLogger(StateHistory.class);

    private Set<HistoryListener> listeners = new HashSet<>();

    public StateHistory() {
        PopStateEventListener listener =
                event -> {
            if(Objects.nonNull(event.getState()))
                inform(event.getState().historyToken, event.getState().title, event.getState().data);
        };
        Window.getSelf().addEventListener("popstate", listener::onPopState);
    }

    private void inform(String token, String title, String stateJson) {
        LOGGER.info("History listeners count : "+listeners.size());
        listeners.stream().filter(l -> l.tokenFilter.filter(token))
                .forEach(l -> ClientApp.make().getAsyncRunner().runAsync(() ->
                        l.listener.onPopState(new DominoHistoryState(token, title, stateJson))));
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
    public StateHistoryToken currentToken() {
        return new StateHistoryToken(getCurrentToken());
    }

    @Override
    public void fireCurrentStateHistory() {
        String token = getCurrentToken();
        History.State state = Window.getSelf().getHistory().getState();
        replaceState(token, Window.getSelf().getDocument().getTitle(),
                stateData(state));
        inform(token, Window.getSelf().getDocument().getTitle(), stateData(state));
    }

    private String getCurrentToken() {
        return Window.getSelf().getLocation().getPathname().substring(1) + Window.getSelf().getLocation().getSearch();
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
