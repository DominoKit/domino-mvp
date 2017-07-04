package com.progressoft.brix.domino.gwt.client.history;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class History {
    public native void back();

    public native void forward();

    public native void pushState(State state, String title, String url);

    public native void replaceState(State state, String title, String url);

    @JsProperty
    public native State getState();

    @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
    public static class State {
        public String historyToken;
        public String data;
        public String title;
    }

    @JsOverlay
    public static State state(String token) {
        State s = new State();
        s.historyToken = token;
        s.data = "";
        s.title=Window.getSelf().getDocument().getTitle();
        return s;
    }

    @JsOverlay
    public static State state(String token, String data) {
        State s = new State();
        s.historyToken = token;
        s.data = data;
        s.title=Window.getSelf().getDocument().getTitle();
        return s;
    }

    @JsOverlay
    public static State state(String token, String title, String data) {
        State s = new State();
        s.historyToken = token;
        s.data = data;
        s.title=title;
        return s;
    }

    @JsType(isNative = true)
    public interface PopStateEvent {
        @JsProperty
        State getState();
    }

    @FunctionalInterface
    public interface PopStateEventListener {
        void onPopState(PopStateEvent event);
    }
}
