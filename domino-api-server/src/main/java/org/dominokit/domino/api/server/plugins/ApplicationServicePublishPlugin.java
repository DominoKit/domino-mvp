package org.dominokit.domino.api.server.plugins;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

public abstract class ApplicationServicePublishPlugin extends BaseDominoLoaderPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServicePublishPlugin.class);

    private Record appServiceRecord;

    @Override
    protected void applyPlugin(CompleteHandler completeHandler) {

        context.getVertxContext()
                .configRetriever()
                .getConfig(configEvent -> {
                    context.getVertxContext()
                            .registerCleanupTask(cleanupTask -> {
                                if (nonNull(appServiceRecord)) {
                                    LOGGER.info("Un-publishing app service record : \n" + JsonObject.mapFrom(appServiceRecord).encodePrettily());
                                    context.getVertxContext().serviceDiscovery()
                                            .unpublish(appServiceRecord, event -> {
                                                if (event.succeeded()) {
                                                    LOGGER.info("App service record was unpublished successfully : \n" + JsonObject.mapFrom(appServiceRecord).encodePrettily());
                                                } else {
                                                    LOGGER.error("Failed to Unpublish app service record : \n" + JsonObject.mapFrom(appServiceRecord).encodePrettily(), event.cause());
                                                }
                                                cleanupTask.next();
                                            });
                                }
                            });

                    Record recordToPublish = getAppServiceRecord(configEvent.result());
                    context.getVertxContext().serviceDiscovery()
                            .publishRecord(recordToPublish, ar -> {
                                if (ar.succeeded()) {
                                    this.appServiceRecord = ar.result();
                                    LOGGER.info("App service record published successfully: ");
                                    LOGGER.info("published record : \n" + JsonObject.mapFrom(this.appServiceRecord).encodePrettily());
                                } else {
                                    LOGGER.error("Failed to publis app service record : \n" + JsonObject.mapFrom(recordToPublish).encodePrettily(), ar.cause());
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
