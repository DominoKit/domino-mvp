package com.progressoft.brix.domino.desktop.client;

import com.progressoft.brix.domino.api.client.async.AsyncRunner;
import io.vertx.core.Vertx;

public class DesktopAsyncRunner implements AsyncRunner {
    @Override
    public void runAsync(AsyncTask asyncTask) {
        try {
            Vertx.vertx().getOrCreateContext().runOnContext(event -> asyncTask.onSuccess());
        } catch (Throwable t) {
            asyncTask.onFailed(t);
        }
    }
}
