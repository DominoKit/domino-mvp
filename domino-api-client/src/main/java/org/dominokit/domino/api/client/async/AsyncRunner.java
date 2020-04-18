package org.dominokit.domino.api.client.async;

import java.util.logging.Level;
import java.util.logging.Logger;

@FunctionalInterface
public interface AsyncRunner {

    Logger LOGGER = Logger.getLogger(AsyncRunner.class.getName());

    interface AsyncTask {
        void onSuccess();

        default void onFailed(Throwable error) {
            LOGGER.log(Level.FINE, "Failed to run async task : ", error);
        }
    }

    void runAsync(AsyncTask asyncTask);
}
