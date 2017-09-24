package com.progressoft.brix.domino.desktop.client;

import com.progressoft.brix.domino.api.client.async.AsyncRunner;

import java.util.concurrent.CompletableFuture;

public class DesktopAsyncRunner implements AsyncRunner {
    @Override
    public void runAsync(AsyncTask asyncTask) {
        try {
            CompletableFuture<AsyncTask> completableFuture=CompletableFuture.supplyAsync(() -> {
                asyncTask.onSuccess();
                return asyncTask;
            });
            completableFuture.complete(asyncTask);
//            DesktopContext.getVertx().getOrCreateContext().runOnContext(event -> {
//                asyncTask.onSuccess();
//            });
        } catch (Throwable t) {
            asyncTask.onFailed(t);
        }
    }
}
