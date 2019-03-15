package org.dominokit.domino.api.server.plugins;

import io.vertx.ext.jdbc.JDBCClient;

public class DBContext {
    static JDBCClient dbClient;

    public static JDBCClient getDbClient() {
        return dbClient;
    }
}
