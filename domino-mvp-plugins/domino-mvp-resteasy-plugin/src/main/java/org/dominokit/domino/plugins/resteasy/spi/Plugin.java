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
package org.dominokit.domino.plugins.resteasy.spi;

import io.reactivex.Completable;
import java.io.IOException;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.jboss.resteasy.spi.HttpRequest;

public abstract class Plugin {
  public Completable preInit() {
    return Completable.complete();
  }

  public Completable init() {
    return Completable.complete();
  }

  public Completable shutdown() {
    return Completable.complete();
  }

  public Completable deployToResteasy(VertxResteasyDeployment deployment) {
    return Completable.complete();
  }

  // FIXME: Looks out of place
  public void aroundRequest(HttpRequest req, RunnableWithException<IOException> continuation)
      throws IOException {
    continuation.run();
  }

  public Completable preRoute() {
    return Completable.complete();
  }

  public Completable postRoute() {
    return Completable.complete();
  }
}
