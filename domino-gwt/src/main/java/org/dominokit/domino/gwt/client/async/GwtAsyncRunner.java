package org.dominokit.domino.gwt.client.async;

import org.dominokit.domino.api.client.async.AsyncRunner;
import org.gwtproject.core.client.Scheduler;

public class GwtAsyncRunner implements AsyncRunner{
    @Override
    public void runAsync(AsyncTask asyncTask) {

        Scheduler.get().scheduleDeferred(() -> {
            try{
                asyncTask.onSuccess();
            }catch (Throwable reason){
                asyncTask.onFailed(reason);
            }
        });

    }
}
