/*
 * Copyright © 2019 Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.api.client.async;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An interface to provide different implementations for different environment to execute async code
 */
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
