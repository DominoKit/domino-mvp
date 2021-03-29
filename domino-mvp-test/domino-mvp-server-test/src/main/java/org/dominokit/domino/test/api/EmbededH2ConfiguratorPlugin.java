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
package org.dominokit.domino.test.api;

import com.google.auto.service.AutoService;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.dominokit.domino.api.server.plugins.BaseDominoLoaderPlugin;

@AutoService(DominoLoaderPlugin.class)
public class EmbededH2ConfiguratorPlugin extends BaseDominoLoaderPlugin {

  @Override
  protected void applyPlugin(CompleteHandler completeHandler) {
    TestDBContext.dbClient =
        JDBCClient.createShared(
            context.getVertx(),
            context
                .getConfig()
                .getJsonObject(
                    "test.jdbc.db.config",
                    new JsonObject()
                        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                        .put("driver_class", "org.hsqldb.jdbcDriver")
                        .put("max_pool_size", 30)));
    completeHandler.onCompleted();
  }

  @Override
  public int order() {
    return 100;
  }

  @Override
  public boolean isEnabled() {
    return context.getConfig().getBoolean("test.embeded.h2.enabled", false);
  }
}
