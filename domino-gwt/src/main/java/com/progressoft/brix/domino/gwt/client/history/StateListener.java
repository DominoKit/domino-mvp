package com.progressoft.brix.domino.gwt.client.history;

import jsinterop.annotations.JsFunction;


@JsFunction
@FunctionalInterface
public interface StateListener<T> {
	void accept(T data);
}
