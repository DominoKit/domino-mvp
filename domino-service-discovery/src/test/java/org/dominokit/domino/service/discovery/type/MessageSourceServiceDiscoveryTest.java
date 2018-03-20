package org.dominokit.domino.service.discovery.type;

import org.dominokit.domino.service.discovery.BaseVertxServiceDiscoveryTest;
import org.dominokit.domino.service.discovery.configuration.AddressableServiceConfiguration;
import org.dominokit.domino.service.discovery.configuration.BaseServiceConfiguration;
import org.dominokit.domino.service.discovery.configuration.MessageSourceConfiguration;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.servicediscovery.Record;
import org.junit.Test;

public class MessageSourceServiceDiscoveryTest extends BaseVertxServiceDiscoveryTest {

    private MessageSourceConfiguration messageSourceConfiguration;

    @Override
    protected void onSetup() {
        messageSourceConfiguration = new MessageSourceConfiguration(SERVICE_NAME, SERVICE_ADDRESS);
    }

    private interface TestMessageType {
    }

    private void publishMessageSourceService(MessageSourceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.messageSource().publish(configuration, handler);
    }

    private void publishMessageSourceService(MessageSourceConfiguration configuration) {
        publishMessageSourceService(configuration, ar -> {
        });
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingMessageSourceWithNullName_thenShouldThrowException() throws Exception {
        publishMessageSourceService(new MessageSourceConfiguration(null, SERVICE_ADDRESS));
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingMessageSourceWithEmptyName_thenShouldThrowException() throws Exception {
        publishMessageSourceService(new MessageSourceConfiguration(EMPTY_STRING, SERVICE_ADDRESS));
    }

    @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
    public void givenServiceDiscovery_whenPublishingMessageSourceWithOnlySpacesName_thenShouldThrowException() throws Exception {
        publishMessageSourceService(new MessageSourceConfiguration(ONLY_SPACES, SERVICE_ADDRESS));
    }

    @Test(expected = AddressableServiceConfiguration.InvalidServiceAddressException.class)
    public void givenServiceDiscovery_whenPublishingMessageSourceWithNullAddress_thenShouldThrowException() throws Exception {
        publishMessageSourceService(new MessageSourceConfiguration(SERVICE_NAME, null));
    }

    @Test(expected = AddressableServiceConfiguration.InvalidServiceAddressException.class)
    public void givenServiceDiscovery_whenPublishingMessageSourceWithEmptyAddress_thenShouldThrowException() throws Exception {
        publishMessageSourceService(new MessageSourceConfiguration(SERVICE_NAME, EMPTY_STRING));
    }

    @Test(expected = AddressableServiceConfiguration.InvalidServiceAddressException.class)
    public void givenServiceDiscovery_whenPublishingMessageSourceWithOnlySpacesAddress_thenShouldThrowException() throws Exception {
        publishMessageSourceService(new MessageSourceConfiguration(SERVICE_NAME, ONLY_SPACES));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingMessageSource_thenServiceMustBePublished(TestContext context) throws Exception {
        publishMessageSourceService(messageSourceConfiguration, context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingMessageSourceWithMessageType_thenServiceMustBePublishedWithThatType(TestContext context) throws Exception {
        publishMessageSourceService(messageSourceConfiguration.messageType(TestMessageType.class), context.asyncAssertSuccess(record -> {
            context.assertEquals(TestMessageType.class.getName(), record.getMetadata().getString("message.type"));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingMessageSourceWithMetadata_thenServiceMustBePublishedWithThatMetadata(TestContext context) throws Exception {
        publishMessageSourceService(messageSourceConfiguration.metadata(metadata), context.asyncAssertSuccess(record -> {
            context.assertEquals(VALUE, record.getMetadata().getString(KEY));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishingMessageSourceAndAskingForItsConsumer_thenShouldGetTheConsumer(TestContext context) throws Exception {
        publishMessageSourceService(messageSourceConfiguration);
        vertxServiceDiscovery.messageSource().getConsumer(record -> record.getName().equals(SERVICE_NAME), context.asyncAssertSuccess());
    }

    @Test
    public void givenServiceDiscovery_whenPublishingMessageSourceAndAskingForItsConsumerByJson_thenShouldGetTheConsumer(TestContext context) throws Exception {
        publishMessageSourceService(messageSourceConfiguration);
        vertxServiceDiscovery.messageSource().getConsumer(new JsonObject().put("name", SERVICE_NAME), context.asyncAssertSuccess());
    }
}
