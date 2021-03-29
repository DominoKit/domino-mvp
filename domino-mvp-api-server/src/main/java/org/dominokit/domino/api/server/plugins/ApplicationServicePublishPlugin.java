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
package org.dominokit.domino.api.server.plugins;

import static java.util.Objects.nonNull;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ApplicationServicePublishPlugin extends BaseDominoLoaderPlugin {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ApplicationServicePublishPlugin.class);

  private Record appServiceRecord;

  @Override
  protected void applyPlugin(CompleteHandler completeHandler) {

    context
        .getVertxContext()
        .configRetriever()
        .getConfig(
            configEvent -> {
              context
                  .getVertxContext()
                  .registerCleanupTask(
                      cleanupTask -> {
                        if (nonNull(appServiceRecord)) {
                          LOGGER.info(
                              "Un-publishing app service record : \n"
                                  + JsonObject.mapFrom(appServiceRecord).encodePrettily());
                          context
                              .getVertxContext()
                              .serviceDiscovery()
                              .unpublish(
                                  appServiceRecord,
                                  event -> {
                                    if (event.succeeded()) {
                                      LOGGER.info(
                                          "App service record was unpublished successfully : \n"
                                              + JsonObject.mapFrom(appServiceRecord)
                                                  .encodePrettily());
                                    } else {
                                      LOGGER.error(
                                          "Failed to Unpublish app service record : \n"
                                              + JsonObject.mapFrom(appServiceRecord)
                                                  .encodePrettily(),
                                          event.cause());
                                    }
                                    cleanupTask.next();
                                  });
                        }
                      });

              Record recordToPublish = getAppServiceRecord(configEvent.result());
              context
                  .getVertxContext()
                  .serviceDiscovery()
                  .publishRecord(
                      recordToPublish,
                      ar -> {
                        if (ar.succeeded()) {
                          this.appServiceRecord = ar.result();
                          LOGGER.info("App service record published successfully: ");
                          LOGGER.info(
                              "published record : \n"
                                  + JsonObject.mapFrom(this.appServiceRecord).encodePrettily());
                        } else {
                          LOGGER.error(
                              "Failed to publis app service record : \n"
                                  + JsonObject.mapFrom(recordToPublish).encodePrettily(),
                              ar.cause());
                        }
                        completeHandler.onCompleted();
                      });
            });
  }

  protected abstract Record getAppServiceRecord(JsonObject config);

  @Override
  public int order() {
    return Integer.MAX_VALUE - 1;
  }

  @Override
  public boolean isEnabled() {
    return context.getConfig().getBoolean("publish.app.as.service", false);
  }
}
