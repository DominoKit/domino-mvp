package org.dominokit.domino.api.shared.history;

public interface DominoHistory {

    DirectState listen(StateListener listener);

    DirectState listen(TokenFilter tokenFilter, StateListener listener);

    DirectState listen(StateListener listener, boolean removeOnComplete);

    DirectState listen(TokenFilter tokenFilter, StateListener listener, boolean removeOnComplete);

    void back();

    void forward();

    void pushState(String token, String title, String data);
    void pushState(String token, String title, String data, TokenParameter... parameters);
    void pushState(String token);
    void pushState(String token, TokenParameter... parameters);
    void fireState(String token, String title, String data);
    void fireState(String token, String title, String data, TokenParameter... parameters);
    void fireState(String token);
    void fireState(String token, TokenParameter... parameters);

    void replaceState(String token, String title, String data);

    HistoryToken currentToken();

    interface StateListener {
        void onPopState(State state);
    }

    interface State {
        HistoryToken token();

        String data();

        String title();

        NormalizedToken normalizedToken();
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
