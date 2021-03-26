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
package org.dominokit.domino.plugins.resteasy;

import com.google.auto.service.AutoService;
import io.vertx.reactivex.ext.auth.User;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import org.dominokit.domino.api.server.*;
import org.dominokit.domino.api.server.plugins.BaseDominoLoaderPlugin;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

@AutoService(DominoLoaderPlugin.class)
public class RestEasyConfiguratorPlugin extends BaseDominoLoaderPlugin {

  @Override
  public void applyPlugin(CompleteHandler completeHandler) {
    VertxResteasyDeployment vertxResteasyDeployment = setupResteasy(loadResources());

    AppGlobals globals = AppGlobals.get();
    globals.setDeployment(vertxResteasyDeployment);

    RestEasyDispatcherHandler restEasyDispatcherHandler =
        new RestEasyDispatcherHandler(context, vertxResteasyDeployment, new ArrayList<>());

    String serviceRoot = context.getConfig().getString("resource.root.path", "/service");
    context
        .getRxRouter()
        .route(serviceRoot + "/*")
        .order(Integer.MAX_VALUE - 1)
        .handler(
            BodyHandler.create()
                .setUploadsDirectory(
                    context.getConfig().getString("file.upload.temp", "file-uploads"))
                .setHandleFileUploads(true))
        .handler(
            routingContext -> {
              ResteasyProviderFactory.pushContext(User.class, routingContext.user());
              ResteasyProviderFactory.pushContext(RoutingContext.class, routingContext);
              ResteasyProviderFactory.pushContext(
                  ResourceContext.class,
                  new ResourceContext(
                      context.getRxVertx(),
                      context.getRxRouter(),
                      context.getVertxContext(),
                      routingContext));
              try {
                restEasyDispatcherHandler.handle(routingContext.getDelegate());
              } catch (Exception ex) {
                routingContext.fail(500, ex);
              }
            });

    completeHandler.onCompleted();
  }

  protected VertxResteasyDeployment setupResteasy(Class<?>... resourceOrProviderClasses) {
    VertxResteasyDeployment deployment = new VertxResteasyDeployment();
    deployment
        .getDefaultContextObjects()
        .put(io.vertx.reactivex.core.Vertx.class, context.getRxVertx());

    for (Class<?> klass : resourceOrProviderClasses) {
      if (klass.isAnnotationPresent(Path.class)) {
        deployment.getActualResourceClasses().add(klass);
      }
    }

    deployment.start();

    return deployment;
  }

  private Class<?>[] loadResources() {
    List<Class<?>> resources = ServerApp.make().resourcesRepository().getResources();
    resources.add(AppResource.class);
    Class<?>[] resourceClasses = new Class[resources.size()];

    resources.toArray(resourceClasses);
    return resourceClasses;
  }

  @Override
  public int order() {
    return PluginContext.RESTEASY_ORDER;
  }

  @Override
  public boolean isEnabled() {
    return context.getConfig().getBoolean("resteasy.enabled", true);
  }
}
