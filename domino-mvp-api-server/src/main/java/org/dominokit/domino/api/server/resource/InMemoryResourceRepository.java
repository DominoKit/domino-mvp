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
package org.dominokit.domino.api.server.resource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryResourceRepository implements ResourcesRepository {
  private final List<Class<?>> handlers = new LinkedList<>();

  @Override
  public List<Class<?>> getResources() {
    return new ArrayList<>(handlers);
  }

  @Override
  public void registerResource(Class<?> resourceClass) {
    if (isResourceExists(resourceClass))
      throw new RequestHandlerHaveAlreadyBeenRegistered(resourceClass.getCanonicalName());
    handlers.add(resourceClass);
  }

  private boolean isResourceExists(Class<?> resourceClass) {
    return handlers.contains(resourceClass);
  }
}
