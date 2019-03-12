package org.dominokit.domino.test.api;

import io.vertx.ext.jdbc.JDBCClient;

public class TestDBContext {
    static JDBCClient dbClient;

    public static JDBCClient getDbClient() {
        return dbClient;
    }
}
