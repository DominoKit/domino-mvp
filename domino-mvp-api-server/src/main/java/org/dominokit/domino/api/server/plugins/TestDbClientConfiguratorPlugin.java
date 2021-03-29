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
package org.dominokit.domino.api.server.plugins;

import static java.util.Objects.nonNull;

import com.google.auto.service.AutoService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLConnection;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.h2.tools.RunScript;

@AutoService(DominoLoaderPlugin.class)
public class TestDbClientConfiguratorPlugin extends BaseDominoLoaderPlugin {

  @Override
  protected void applyPlugin(CompleteHandler completeHandler) {
    JsonObject dbConfigurations =
        context
            .getConfig()
            .getJsonObject(
                "jdbc.db.config",
                new JsonObject()
                    .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                    .put("driver_class", "org.hsqldb.jdbcDriver")
                    .put("max_pool_size", 30));
    DBContext.dbClient = JDBCClient.createShared(context.getRxVertx(), dbConfigurations);

    String dbScriptFile = dbConfigurations.getString("db_script_file");
    if (nonNull(dbScriptFile)) {
      context
          .getRxVertx()
          .fileSystem()
          .rxReadFile(dbScriptFile)
          .flatMapCompletable(this::runScript)
          .doFinally(completeHandler::onCompleted)
          .subscribe();
    } else {
      completeHandler.onCompleted();
    }
  }

  private Completable runScript(Buffer buffer) {
    return DBContext.dbClient
        .rxGetConnection()
        .flatMapCompletable(sqlConnection -> doRunScript(buffer, sqlConnection));
  }

  private CompletableSource doRunScript(Buffer buffer, SQLConnection sqlConnection) {
    try (Reader targetReader = new StringReader(buffer.toString())) {
      RunScript.execute(sqlConnection.getDelegate().unwrap(), targetReader);
      return Completable.complete();
    } catch (SQLException | IOException e) {
      return Completable.error(e);
    }
  }

  @Override
  public int order() {
    return 100;
  }

  @Override
  public boolean isEnabled() {
    return context.getConfig().getBoolean("jdbc.client.enabled", false);
  }
}
