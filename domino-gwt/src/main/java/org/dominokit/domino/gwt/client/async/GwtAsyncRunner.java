package org.dominokit.domino.gwt.client.async;

//import com.google.gwt.core.client.Scheduler;
import org.dominokit.domino.api.client.async.AsyncRunner;

public class GwtAsyncRunner implements AsyncRunner{
    @Override
    public void runAsync(AsyncTask asyncTask) {
//        Scheduler.get().scheduleDeferred(() -> {
            try{
                asyncTask.onSuccess();
            }catch (Throwable reason){
                asyncTask.onFailed(reason);
            }
//        });
//        GWT.runAsync(new RunAsyncCallback() {
//            @Override
//            public void onFailure(Throwable reason) {
//                asyncTask.onFailed(reason);
//            }
//
//            @Override
//            public void onSuccess() {
//                asyncTask.onSuccess();
//            }
//        });
    }
}
