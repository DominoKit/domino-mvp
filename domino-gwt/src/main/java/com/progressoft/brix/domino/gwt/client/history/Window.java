package com.progressoft.brix.domino.gwt.client.history;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "window")
public class Window {
	@JsProperty
	public native static Window getSelf();

	@JsProperty
	public native Document getDocument();

	@JsProperty
	public native History getHistory();

	@JsProperty
	public native Location getLocation();

	public native <T> void addEventListener(String type, StateListener<T> listenerFunction);
}
