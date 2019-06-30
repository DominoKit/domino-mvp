package org.dominokit.domino.api.server.plugins;

import com.google.auto.service.AutoService;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLConnection;
import org.dominokit.domino.api.server.DominoLoaderPlugin;
import org.h2.tools.RunScript;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;

@AutoService(DominoLoaderPlugin.class)
public class TestDbClientConfiguratorPlugin extends BaseDominoLoaderPlugin {

    @Override
    protected void applyPlugin(CompleteHandler completeHandler) {
        JsonObject dbConfigurations = context.getConfig().getJsonObject("jdbc.db.config",
                new JsonObject()
                        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                        .put("driver_class", "org.hsqldb.jdbcDriver")
                        .put("max_pool_size", 30));
        DBContext.dbClient = JDBCClient.createShared(context.getRxVertx(), dbConfigurations);

        String dbScriptFile = dbConfigurations.getString("db_script_file");
        context.getRxVertx()
                .fileSystem()
                .rxReadFile(dbScriptFile)
                .flatMapCompletable(this::runScript)
                .doFinally(completeHandler::onCompleted)
                .subscribe();
    }

    private Completable runScript(Buffer buffer) {
        return DBContext.dbClient.rxGetConnection()
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
