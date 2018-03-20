package org.dominokit.domino.api.shared.history;

public interface DominoHistory {

    DirectState listen(StateListener listener);

    DirectState listen(TokenFilter tokenFilter, StateListener listener);

    void back();

    void forward();

    void pushState(String token, String title, String data);
    void pushState(String token);

    void replaceState(String token, String title, String data);

    HistoryToken currentToken();

    interface StateListener {
        void onPopState(State state);
    }

    interface State {
        HistoryToken token();

        String data();

        String title();
    }

    @FunctionalInterface
    interface DirectUrlHandler {
        void handle(State state);
    }

    @FunctionalInterface
    interface DirectState{
        void onDirectUrl(DirectUrlHandler handler);
    }
}
