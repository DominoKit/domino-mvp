/*
 * Copyright © 2019 Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.plugins.resteasy;

import static java.util.Objects.isNull;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Router;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.dominokit.domino.api.server.GlobalsProvider;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class AppGlobals {

  private static ThreadLocal<AppGlobals> global = new ThreadLocal<AppGlobals>();

  private Map<String, Object> namedGlobals = new HashMap<>();
  private Map<Class<?>, Object> typedGlobals = new HashMap<>();
  private VertxResteasyDeployment deployment;

  static AppGlobals init() {
    AppGlobals globals = new AppGlobals();
    global.set(globals);
    return globals;
  }

  public static AppGlobals get() {
    if (isNull(global.get())) {
      init();
    }
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

  public JsonObject getConfig() {
    return GlobalsProvider.INSTANCE.getConfig();
  }

  public Vertx getVertx() {
    return GlobalsProvider.INSTANCE.getVertx();
  }

  public Router getRouter() {
    return GlobalsProvider.INSTANCE.getRouter();
  }

  public void setGlobal(String key, Object value) {
    namedGlobals.put(key, value);
  }

  public Object getGlobal(String key) {
    return namedGlobals.get(key);
  }

  public <T> void setGlobal(Class<T> klass, T value) {
    typedGlobals.put(klass, value);
  }

  public <T> T getGlobal(Class<T> klass) {
    return (T) typedGlobals.get(klass);
  }

  public void injectGlobals() {
    // FIXME: inject using the more precise Type using the new resteasy injection API
    for (Entry<Class<?>, Object> entry : typedGlobals.entrySet()) {
      ResteasyProviderFactory.pushContext((Class) entry.getKey(), entry.getValue());
    }
  }

  public void setDeployment(VertxResteasyDeployment deployment) {
    this.deployment = deployment;
  }

  public VertxResteasyDeployment getDeployment() {
    return deployment;
  }
}
