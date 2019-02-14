package org.dominokit.domino.api.server;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AppGlobals {
	
	private static ThreadLocal<AppGlobals> global = new ThreadLocal<AppGlobals>();

	static AppGlobals init() {
		AppGlobals globals = new AppGlobals();
		global.set(globals);
		return globals;
	}

	public static AppGlobals get() {
		return global.get();
	}

	public static AppGlobals set(AppGlobals globals) {
		AppGlobals old = global.get();
		global.set(globals);
		return old;
	}

	public static void clear() {
		global.remove();
	}

	private JsonObject config;
	private Vertx vertx;
	private Router router;
	private Map<String, Object> namedGlobals = new HashMap<>();
	private Map<Class<?>, Object> typedGlobals = new HashMap<>();
	private VertxResteasyDeployment deployment;

	public JsonObject getConfig() {
		return config;
	}

	void setConfig(JsonObject config) {
		this.config = config;
	}


	void setVertx(Vertx vertx) {
		this.vertx = vertx;
	}

	public Vertx getVertx() {
		return vertx;
	}

	void setRouter(Router router) {
		this.router = router;
	}
	
	public Router getRouter() {
		return router;
	}


	public void setGlobal(String key, Object value) {
		namedGlobals.put(key, value);
	}
	
	public Object getGlobal(String key) {
		return namedGlobals.get(key);
	}
	
	public <T> void setGlobal(Class<T> klass, T value){
		typedGlobals.put(klass, value);
	}

	public <T> T getGlobal(Class<T> klass){
		return (T) typedGlobals.get(klass);
	}
	
	public void injectGlobals() {
		// FIXME: inject using the more precise Type using the new resteasy injection API
		for (Entry<Class<?>, Object> entry : typedGlobals.entrySet()) {
			ResteasyProviderFactory.pushContext((Class)entry.getKey(), entry.getValue());
		}
	}

	public void setDeployment(VertxResteasyDeployment deployment) {
		this.deployment = deployment;
	}
	
	public VertxResteasyDeployment getDeployment() {
		return deployment;
	}
}
