package org.dominokit.domino.gwt.client.history;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Location {

	public native void assign(String newLocation);
	public native void replace(String newLocation);
	public native void reload();
	public native String toString();

	@JsProperty
	public native String getHref();
	@JsProperty
	public native String getProtocol();
	@JsProperty
	public native String getHost();
	@JsProperty
	public native String getHostname();
	@JsProperty
	public native String getPort();
	@JsProperty
	public native String getPathname();
	@JsProperty
	public native String getSearch();
	@JsProperty
	public native String getHash();
	@JsProperty
	public native String getUsername();
	@JsProperty
	public native String getPassword();
	@JsProperty
	public native String getOrigin();
}
