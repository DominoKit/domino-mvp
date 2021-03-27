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
package org.dominokit.domino.api.server;

import static java.util.Objects.isNull;

import org.dominokit.domino.api.server.config.ServerModuleConfiguration;
import org.dominokit.domino.api.server.entrypoint.ServerContext;
import org.dominokit.domino.api.server.resource.ResourceRegistry;
import org.dominokit.domino.api.server.resource.ResourcesRepository;

public class ServerApp implements ResourceRegistry {

  private static AttributeHolder<ResourcesRepository> resourcesRepositoryHolder =
      new AttributeHolder<>();
  private static AttributeHolder<ServerContext> serverContextHolder = new AttributeHolder<>();

  private ServerApp() {}

  public static ServerApp make() {
    return new ServerApp();
  }

  public ServerApp run() {
    return this;
  }

  @Override
  public void registerResource(Class<?> resourceClass) {
    resourcesRepositoryHolder.attribute.registerResource(resourceClass);
  }

  public ResourcesRepository resourcesRepository() {
    return resourcesRepositoryHolder.attribute;
  }

  public ServerContext serverContext() {
    return serverContextHolder.attribute;
  }

  public void configureModule(ServerModuleConfiguration configuration) {
    configuration.registerResources(this);
  }

  public static class ServerAppBuilder {

    private ResourcesRepository resourcesRepository;
    private ServerContext serverContext;

    public ServerAppBuilder handlersRepository(ResourcesRepository resourcesRepository) {
      this.resourcesRepository = resourcesRepository;
      return this;
    }

    public ServerAppBuilder serverContext(ServerContext serverContext) {
      this.serverContext = serverContext;
      return this;
    }

    public ServerApp build() {
      if (isNull(resourcesRepository)) throw new HandlersRepositoryIsRequired();
      if (isNull(serverContext)) throw new ServerContextIsRequired();

      ServerApp.resourcesRepositoryHolder.hold(resourcesRepository);
      ServerApp.serverContextHolder.hold(serverContext);

      return new ServerApp();
    }

    private class HandlersRepositoryIsRequired extends RuntimeException {}

    private class ServerContextIsRequired extends RuntimeException {}
  }

  private static final class AttributeHolder<T> {
    private T attribute;

    private void hold(T attribute) {
      this.attribute = attribute;
    }
  }
}
