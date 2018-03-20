package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.DataSourceServiceDiscoveryTest;
import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.servicediscovery.Record;
import org.junit.Test;

public class RedisServiceDiscoveryTest extends DataSourceServiceDiscoveryTest {

    private void publishRedisDataSourceService(DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.redis().publish(configuration, handler);
    }

    private void publishRedisDataSourceService(DataSourceServiceConfiguration configuration) {
        vertxServiceDiscovery.redis().publish(configuration, ar -> {
        });
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataSourceService_thenTheServiceShouldBePublished(TestContext context) throws Exception {
        publishRedisDataSourceService(dataSourceConfiguration, context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataSourceServiceWithMetadata_thenTheServiceShouldBePublishedWithThatMetadata(TestContext context) throws Exception {
        publishRedisDataSourceService(dataSourceConfiguration.metadata(metadata), context.asyncAssertSuccess(record -> {
            context.assertEquals(VALUE, record.getMetadata().getString(KEY));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataSourceAndAskingForItsClient_thenShouldGetTheClient(TestContext context) throws Exception {
        publishRedisDataSourceService(dataSourceConfiguration);
        vertxServiceDiscovery.redis().getClient(filter(), context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataAndAskingForItsClientWithConfiguration_thenShouldGetTheClient(TestContext context) throws Exception {
        publishRedisDataSourceService(dataSourceConfiguration.metadata(metadata));
        vertxServiceDiscovery.redis().getClient(filter(), metadata, context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataAndAskingForItsClientByJson_thenShouldGetTheClient(TestContext context) throws Exception {
        publishRedisDataSourceService(dataSourceConfiguration);
        vertxServiceDiscovery.redis().getClient(jsonFilter, context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRedisDataAndAskingForItsClientByJsonWithConfiguration_thenShouldGetTheClient(TestContext context) throws Exception {
        publishRedisDataSourceService(dataSourceConfiguration.metadata(metadata));
        vertxServiceDiscovery.redis().getClient(jsonFilter, metadata, context.asyncAssertSuccess());
    }
}
