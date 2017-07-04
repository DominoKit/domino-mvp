package com.progressoft.brix.domino.gwt.client.history;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Document {
	@JsProperty
	public native String getTitle();
}
