package org.dominokit.domino.api.server.plugins;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class MultiAppListenerPlugin extends BaseDominoLoaderPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiAppListenerPlugin.class);

    @Override
    protected void applyPlugin(CompleteHandler completeHandler) {
        context.getVertxContext()
                .vertx()
                .eventBus()
                .<JsonObject>consumer(ServiceDiscoveryOptions.DEFAULT_ANNOUNCE_ADDRESS, message -> {
                    LOGGER.info("Application status event received : \n" + message.body().encodePrettily());
                    Record record = new Record();
                    record.setName(message.body().getString("name"));
                    record.setLocation(message.body().getJsonObject("location"));
                    record.setStatus(Status.valueOf(message.body().getString("status")));
                    record.setType(message.body().getString("type"));
                    record.setMetadata(message.body().getJsonObject("metadata"));

                    if (Status.UP.equals(record.getStatus())) {
                        LOGGER.info("Application service [" + record.getName() + "] Is UP.!");
                        onAppServiceUp(record);
                    } else {
                        LOGGER.info("Application service[" + record.getName() + "] went DOWN.!");
                        onAppServiceDown(record);
                    }
                });

        context.getVertxContext().serviceDiscovery()
                .lookupAll(record -> true, event -> {
                    if (event.succeeded()) {
                        List<Record> appRecords = event.result();
                        appRecords.forEach(record -> {
                            LOGGER.info("Found the following application service record: \n" + JsonObject.mapFrom(record).encodePrettily());
                            if (Status.UP.equals(record.getStatus())) {
                                onAppServiceUp(record);
                            }
                        });
                    } else {
                        LOGGER.error("Could not lookup running applications services.! ", event.cause());
                    }
                    completeHandler.onCompleted();
                });
    }

    protected abstract void onAppServiceUp(Record record);

    protected abstract void onAppServiceDown(Record record);

    @Override
    public int order() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public boolean isEnabled() {
        return context.getConfig().getBoolean("multi.app.support", false);
    }
}
