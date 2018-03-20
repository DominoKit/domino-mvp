package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.async.AsyncRunner;

public class TestAsyncRunner implements AsyncRunner {
    @Override
    public void runAsync(AsyncTask asyncTask) {
        try{
            asyncTask.onSuccess();
        }catch (Exception e){
            asyncTask.onFailed(e);
        }
    }
}
