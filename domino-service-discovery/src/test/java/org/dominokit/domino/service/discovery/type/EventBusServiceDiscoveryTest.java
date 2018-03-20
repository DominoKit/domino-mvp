package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.BaseVertxServiceDiscoveryTest;
import org.dominokit.domino.service.discovery.configuration.AddressableServiceConfiguration;
import org.dominokit.domino.service.discovery.configuration.BaseServiceConfiguration;
import org.dominokit.domino.service.discovery.configuration.EventBusServiceConfiguration;
import org.dominokit.domino.service.discovery.generation.TestService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.servicediscovery.Record;
import org.junit.Assert;
import org.junit.Test;

public class EventBusServiceDiscoveryTest extends BaseVertxServiceDiscoveryTest {

    private EventBusServiceConfiguration eventBusConfiguration;

    @Override
    protected void onSetup() {
        eventBusConfiguration = new EventBusServiceConfiguration(SERVICE_NAME, SERVICE_ADDRESS, TestService.class);
    }

    private void publishEventBusService(EventBusServiceConfiguration configuration) {
        publishEventBusService(configuration, ar -> {
        });
    }

    private void publishEventBusService(EventBusServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.eventBus().publish(configuration, handler);
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithNullName_thenShouldThrowException() throws Exception {
        publishEventBusService(new EventBusServiceConfiguration(null, SERVICE_ADDRESS, TestService.class));
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithEmptyName_thenShouldThrowException() throws Exception {
        publishEventBusService(new EventBusServiceConfiguration(EMPTY_STRING, SERVICE_ADDRESS, TestService.class));
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithOnlySpacesName_thenShouldThrowException() throws Exception {
        publishEventBusService(new EventBusServiceConfiguration(ONLY_SPACES, SERVICE_ADDRESS, TestService.class));
    }

    @Test(expected = AddressableServiceConfiguration.InvalidServiceAddressException.class)
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithNullAddress_thenShouldThrowException() throws Exception {
        publishEventBusService(new EventBusServiceConfiguration(SERVICE_NAME, null, TestService.class));
    }

    @Test(expected = AddressableServiceConfiguration.InvalidServiceAddressException.class)
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithEmptyAddress_thenShouldThrowException() throws Exception {
        publishEventBusService(new EventBusServiceConfiguration(SERVICE_NAME, EMPTY_STRING, TestService.class));
    }

    @Test(expected = AddressableServiceConfiguration.InvalidServiceAddressException.class)
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithOnlySpacesAddress_thenShouldThrowException() throws Exception {
        publishEventBusService(new EventBusServiceConfiguration(SERVICE_NAME, ONLY_SPACES, TestService.class));
    }

    @Test(expected = EventBusServiceConfiguration.NullServiceClassException.class)
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithNullServiceClass_thenShouldThrowException() throws Exception {
        publishEventBusService(new EventBusServiceConfiguration(SERVICE_NAME, SERVICE_ADDRESS, null));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingEventBusService_thenTheServiceShouldBePublished(TestContext context) throws Exception {
        publishEventBusService(eventBusConfiguration, context.asyncAssertSuccess(record -> {
            context.assertEquals(SERVICE_NAME, record.getName());
            context.assertEquals(SERVICE_ADDRESS, record.getLocation().getString("endpoint"));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingEventBusServiceWithMetadata_thenTheServiceShouldBePublishedWithThatMetadata(TestContext context) throws Exception {
        publishEventBusService(eventBusConfiguration.metadata(metadata), context.asyncAssertSuccess(record -> {
            context.assertEquals(VALUE, record.getMetadata().getString(KEY));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishEventBusServiceAndAskingForItsProxy_thenShouldGetTheService(TestContext context) throws Exception {
        publishEventBusService(eventBusConfiguration);
        vertxServiceDiscovery.eventBus().getProxy(TestService.class, context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishEventBusServiceAndAskingForItsServiceProxy_thenShouldGetTheService(TestContext context) throws Exception {
        publishEventBusService(eventBusConfiguration);
        vertxServiceDiscovery.eventBus().getServiceProxy(record -> record.getName().equals(SERVICE_NAME), TestService.class, context.asyncAssertSuccess(Assert::assertNotNull));
    }

    @Test
    public void givenServiceDiscovery_whenPublishEventBusServiceAndAskingForItsServiceProxyByJson_thenShouldGetTheService(TestContext context) throws Exception {
        publishEventBusService(eventBusConfiguration);
        vertxServiceDiscovery.eventBus().getServiceProxy(jsonFilter, TestService.class, context.asyncAssertSuccess(Assert::assertNotNull));
    }
}
