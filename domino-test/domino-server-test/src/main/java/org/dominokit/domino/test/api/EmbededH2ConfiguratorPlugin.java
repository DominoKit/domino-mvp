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
        TestDBContext.dbClient = JDBCClient.createShared(context.getVertx(),
                context.getConfig().getJsonObject("test.jdbc.db.config",
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
