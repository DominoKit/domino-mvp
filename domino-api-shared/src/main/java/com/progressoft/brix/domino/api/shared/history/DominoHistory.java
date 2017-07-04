package com.progressoft.brix.domino.api.shared.history;

public interface DominoHistory {

    void listen(StateListener listener);

    void listen(StateListener listener, TokenFilter tokenFilter);

    void back();

    void forward();

    void pushState(String token, String title, String data);

    void replaceState(String token, String title, String data);

    HistoryToken currentToken();

    interface StateListener {
        void onPopState(State state);
    }

    interface State{
        HistoryToken token();
        String data();
        String title();
    }
}
