package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.DataSourceServiceDiscoveryTest;
import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.servicediscovery.Record;
import org.junit.Test;

public class MongoServiceDiscoveryTest extends DataSourceServiceDiscoveryTest {

    private void publishMongoDataSourceService(DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.mongo().publish(configuration, handler);
    }

    private void publishMongoDataSourceService(DataSourceServiceConfiguration configuration) {
        vertxServiceDiscovery.mongo().publish(configuration, ar -> {
        });
    }

    @Test
    public void givenServiceDiscovery_whenPublishingMongoDataSourceService_thenTheServiceShouldBePublished(TestContext context) throws Exception {
        publishMongoDataSourceService(dataSourceConfiguration, context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingMongoDataSourceServiceWithMetadata_thenTheServiceShouldBePublishedWithThatMetadata(TestContext context) throws Exception {
        publishMongoDataSourceService(dataSourceConfiguration.metadata(metadata), context.asyncAssertSuccess(record -> {
            context.assertEquals(VALUE, record.getMetadata().getString(KEY));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataSourceAndAskingForItsClient_thenShouldGetTheClient(TestContext context) throws Exception {
        publishMongoDataSourceService(dataSourceConfiguration);
        vertxServiceDiscovery.mongo().getClient(filter(), context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataAndAskingForItsClientByJson_thenShouldGetTheClient(TestContext context) throws Exception {
        publishMongoDataSourceService(dataSourceConfiguration);
        vertxServiceDiscovery.mongo().getClient(jsonFilter, context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataAndAskingForItsClientByJsonWithConfiguration_thenShouldGetTheClient(TestContext context) throws Exception {
        publishMongoDataSourceService(dataSourceConfiguration.metadata(metadata));
        vertxServiceDiscovery.mongo().getClient(jsonFilter, metadata, context.asyncAssertSuccess());
    }

}
