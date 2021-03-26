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
package org.dominokit.domino.api.server.entrypoint;

import static java.util.Objects.nonNull;

import java.util.function.Consumer;

public class CleanupTask {

  private final Consumer<CleanupTask> operation;
  private CleanupTask next;
  private Consumer<CleanupTask> completeConsumer = cleanupTask -> {};
  private final VertxContext vertxContext;

  public CleanupTask(Consumer<CleanupTask> operation, VertxContext vertxContext) {
    this.operation = operation;
    this.vertxContext = vertxContext;
  }

  public void setNext(CleanupTask next) {
    this.next = next;
  }

  public void setCompleteConsumer(Consumer<CleanupTask> completeConsumer) {
    this.completeConsumer = completeConsumer;
  }

  public Consumer<CleanupTask> getCleanUpService() {
    return operation;
  }

  public VertxContext getVertxContext() {
    return vertxContext;
  }

  public void run() {
    operation.accept(this);
  }

  public void next() {
    if (nonNull(next)) {
      next.run();
    } else {
      completeConsumer.accept(this);
    }
  }
}
