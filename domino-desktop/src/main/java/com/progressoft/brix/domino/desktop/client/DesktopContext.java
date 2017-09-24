package com.progressoft.brix.domino.desktop.client;

import io.vertx.core.Vertx;

public class DesktopContext {

    private static Vertx vertx;

    public static Vertx getVertx() {
        return vertx;
    }

    public static void setVertx(Vertx vertx) {
        DesktopContext.vertx = vertx;
    }
}
