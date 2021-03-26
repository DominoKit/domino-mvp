/*
 * Copyright © ${year} Dominokit
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
package org.dominokit.domino.api.server;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import io.vertx.core.AbstractVerticle;
import java.util.Iterator;
import java.util.List;
import org.dominokit.domino.api.server.entrypoint.CleanupTask;
import org.dominokit.domino.api.server.entrypoint.VertxContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(StartupVerticle.class);
  private VertxContext context;
  private boolean cleanupCompleted = false;
  private int retryCount = 0;
  CleanupTask first = null;

  @Override
  public void start() {
    LOGGER.info("Starting verticle --->");
    context =
        new DominoLoader(
                vertx, DominoLauncher.routerHolder.router, DominoLauncher.configHolder.config)
            .start();

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  try {
                    while (!cleanupCompleted && retryCount < 50) {
                      retryCount += 1;
                      LOGGER.info("Waiting for cleanup services to complete, sleep for 100ms ... ");
                      Thread.sleep(100L);
                    }
                  } catch (Exception e) {
                    LOGGER.error("Failed to wait for vertx to stop", e);
                  }
                }));
  }

  @Override
  public void stop() throws Exception {

    List<CleanupTask> cleanupTasks = context.getCleanupTasks();
    if (cleanupTasks.isEmpty()) {
      cleanupCompleted = true;
    } else {
      Iterator<CleanupTask> iterator = cleanupTasks.iterator();
      CleanupTask last = null;
      while (iterator.hasNext()) {
        if (isNull(first)) {
          first = iterator.next();
          last = first;
        } else {
          CleanupTask next = iterator.next();
          last.setNext(next);
          last = next;
        }
      }

      if (nonNull(first)) {
        last.setCompleteConsumer(
            cleanupTask ->
                context
                    .vertx()
                    .close(
                        event -> {
                          cleanupCompleted = true;
                        }));
        LOGGER.info("Stopping verticle <-----");
        first.run();
      }
    }
  }
}
