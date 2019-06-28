package org.dominokit.domino.api.server.plugins;

import io.vertx.reactivex.ext.jdbc.JDBCClient;

public class DBContext {
    static JDBCClient dbClient;

    public static JDBCClient getDbClient() {
        return dbClient;
    }

    public static void setDbClient(JDBCClient dbClient) {
        DBContext.dbClient = dbClient;
    }
}
