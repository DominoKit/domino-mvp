package org.dominokit.domino.gwt.client.history;

import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class JsState {
    public String historyToken;
    public String data;
    public String title;

    @JsOverlay
    public static JsState state(String token) {
        JsState state=new JsState();
        state.historyToken = token;
        state.data = "";
        state.title= DomGlobal.document.title;
        return state;
    }

    @JsOverlay
    public static JsState state(String token, String data) {
        JsState state=new JsState();
        state.historyToken = token;
        state.data = data;
        state.title=DomGlobal.document.title;

        return state;
    }

    @JsOverlay
    public static JsState state(String token, String title, String data) {
        JsState state=new JsState();
        state.historyToken = token;
        state.data = data;
        state.title=title;

        return state;
    }
}