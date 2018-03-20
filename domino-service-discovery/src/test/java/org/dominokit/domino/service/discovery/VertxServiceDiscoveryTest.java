package org.dominokit.domino.service.discovery;

import org.dominokit.domino.service.discovery.generation.TestService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.Status;
import io.vertx.servicediscovery.types.EventBusService;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.dominokit.domino.service.discovery.VertxServiceDiscovery.InvalidFilterException;
import static org.junit.Assert.*;

public class VertxServiceDiscoveryTest extends BaseVertxServiceDiscoveryTest {

    private static final String NOT_EXISTED_NAME = "not existed name";

    private Record record;
    private Record otherRecord;

    @Override
    protected void onSetup() {
        metadata.put("service.interface", TestService.class.getName());
        record = new Record().setName(SERVICE_NAME).setType(EventBusService.TYPE)
                .setMetadata(metadata).setLocation(new JsonObject().put("endpoint", "endpoint"));
        otherRecord = new Record().setName("other service name");
    }

    private void publishTestRecord(Record record) {
        publishTestRecord(record, ar -> {
        });
    }

    private void publishTestRecord(Record record, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.publishRecord(record, handler);
    }

    private void lookup(Function<Record, Boolean> filter, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.lookup(filter, handler);
    }

    private void lookupIncludeOutOfService(Function<Record, Boolean> filter, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.lookupIncludeOutOfService(filter, handler);
    }

    private void lookupByJson(JsonObject jsonFilter, Handler<AsyncResult<Record>> handler) {
        vertxServiceDiscovery.lookupByJson(jsonFilter, handler);
    }

    private void lookupAll(Function<Record, Boolean> filter, Handler<AsyncResult<List<Record>>> handler) {
        vertxServiceDiscovery.lookupAll(filter, handler);
    }

    private void lookupAllIncludeOutOfService(Function<Record, Boolean> filter, Handler<AsyncResult<List<Record>>> handler) {
        vertxServiceDiscovery.lookupAllIncludeOutOfService(filter, handler);
    }

    private void lookupAllByJson(JsonObject jsonFilter, Handler<AsyncResult<List<Record>>> handler) {
        vertxServiceDiscovery.lookupAllByJson(jsonFilter, handler);
    }

    @Test
    public void givenServiceDiscovery_whenPublishingRecord_thenTheRecordShouldBePublished(TestContext context) throws Exception {
        vertxServiceDiscovery.publishRecord(record, context.asyncAssertSuccess());
    }

    @Test(expected = InvalidFilterException.class)
    public void givenServiceDiscovery_whenLookupForARecordWithNullFilter_thenShouldThrowException() throws Exception {
        lookup(null, ar -> {
        });
    }

    @Test
    public void givenServiceDiscovery_whenLookupForARecordUnmatchingPredicate_thenShouldGetNullRecord(TestContext context) throws Exception {
        publishTestRecord(record);
        lookup(r -> r.getName().equals(NOT_EXISTED_NAME), context.asyncAssertSuccess(context::assertNull));
    }

    @Test
    public void givenServiceDiscovery_whenLookupForARecordMatchingPredicate_thenShouldGetTheRecord(TestContext context) throws Exception {
        publishTestRecord(record);
        lookup(r -> r.getName().equals(SERVICE_NAME), context.asyncAssertSuccess(record -> {
            context.assertEquals(SERVICE_NAME, record.getName());
        }));
    }

    @Test(expected = InvalidFilterException.class)
    public void givenServiceDiscovery_whenLookupForARecordIncludingOutOfServicePassingNullFilter_thenShouldThrowException(TestContext context) throws Exception {
        lookupIncludeOutOfService(null, ar -> {
        });
    }

    @Test
    public void givenServiceDiscovery_whenLookupForARecordIncludingOutOfService_thenShouldGetTheRecord(TestContext context) throws Exception {
        publishTestRecord(record.setStatus(Status.OUT_OF_SERVICE));
        lookupIncludeOutOfService(r -> r.getName().equals(SERVICE_NAME), context.asyncAssertSuccess(record -> {
            context.assertEquals(Status.OUT_OF_SERVICE, record.getStatus());
        }));
    }

    @Test
    public void givenServiceDiscovery_whenLookupByJsonForARecordPassingNull_thenShouldGetTheRecord(TestContext context) throws Exception {
        publishTestRecord(record);
        lookupByJson(null, context.asyncAssertSuccess(record -> {
            context.assertEquals(SERVICE_NAME, record.getName());
        }));
    }

    @Test
    public void givenServiceDiscovery_whenLookupByJsonForARecordWithAName_thenShouldGetARecordWithThatName(TestContext context) throws Exception {
        publishTestRecord(record);
        lookupByJson(jsonFilter, context.asyncAssertSuccess(record -> {
            context.assertEquals(SERVICE_NAME, record.getName());
        }));
    }

    @Test(expected = InvalidFilterException.class)
    public void givenServiceDiscovery_whenLookupForAllRecordsPassingNullFilter_thenShouldThrowException() throws Exception {
        lookupAll(null, ar -> {
        });
    }

    @Test
    public void givenServiceDiscovery_whenLookupForAllRecordsWithUnmatchingPredicate_thenShouldNotGetAnyRecord(TestContext context) throws Exception {
        publishTestRecord(record);
        publishTestRecord(otherRecord);
        lookupAll(record -> record.getName().equals(NOT_EXISTED_NAME), context.asyncAssertSuccess(records -> {
            context.assertTrue(records.isEmpty());
        }));
    }

    @Test
    public void givenServiceDiscovery_whenLookupForAllRecordsWithMatchingPredicate_thenShouldGetAllRecordsMatching(TestContext context) throws Exception {
        publishTestRecord(record);
        publishTestRecord(otherRecord);
        lookupAll(record -> record.getName().contains(SERVICE_NAME), context.asyncAssertSuccess(records -> {
            context.assertEquals(2, records.size());
        }));
    }

    @Test(expected = InvalidFilterException.class)
    public void givenServiceDiscovery_whenLookupForAllRecordsIncludingOutOfServicePassingNullFilter_thenShouldThrowException(TestContext context) throws Exception {
        lookupAllIncludeOutOfService(null, ar -> {
        });
    }

    @Test
    public void givenServiceDiscovery_whenLookupForAllRecordsIncludingOutOfService_thenShouldGetAllRecordsIncludingOutOfService(TestContext context) throws Exception {
        publishTestRecord(record.setStatus(Status.OUT_OF_SERVICE));
        publishTestRecord(otherRecord);
        lookupAllIncludeOutOfService(record -> record.getName().contains(SERVICE_NAME), context.asyncAssertSuccess(records -> {
            context.assertEquals(2, records.size());
        }));
    }

    @Test
    public void givenServiceDiscovery_whenLookupForAllRecordsByJsonPassingNull_thenShouldGetAllRecords(TestContext context) throws Exception {
        publishTestRecord(record);
        publishTestRecord(otherRecord);
        lookupAllByJson(null, context.asyncAssertSuccess(records -> {
            context.assertEquals(2, records.size());
        }));
    }

    @Test
    public void givenServiceDiscovery_whenLookupForAllRecordsWithNameByJson_thenShouldGetAllRecordsMatching(TestContext context) throws Exception {
        publishTestRecord(record.setMetadata(metadata));
        publishTestRecord(otherRecord.setMetadata(metadata));
        lookupAllByJson(metadata, context.asyncAssertSuccess(records -> {
            context.assertEquals(2, records.size());
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishServiceAndLookupForItsServiceReference_thenShouldGetTheReference(TestContext context) throws Exception {
        publishTestRecord(record, context.asyncAssertSuccess(record -> {
            assertNotNull(vertxServiceDiscovery.lookupForAReference(record));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenPublishServiceWithMetadataAndLookupForItsServiceReferencePassingConfiguration_thenShouldGetTheReferenceUponTheseConfigurations(TestContext context) throws Exception {
        publishTestRecord(record, context.asyncAssertSuccess(record -> {
            ServiceReference reference = vertxServiceDiscovery.lookupForAReferenceWithConfiguration(record, metadata);
            assertEquals(VALUE, reference.record().getMetadata().getString(KEY));
        }));
    }

    @Test
    public void givenServiceDiscovery_whenReleasingServiceObject_thenServiceDiscoveryShouldRemoveThisObjectFromBindings(TestContext context) throws Exception {
        publishTestRecord(record, context.asyncAssertSuccess(record -> {
            ServiceReference serviceReference = vertxServiceDiscovery.lookupForAReference(record);
            TestService service = serviceReference.get();
            vertxServiceDiscovery.releaseServiceObject(service);
            assertTrue(serviceDiscovery.bindings().stream().noneMatch(sr -> sr.isHolding(service)));
        }));
    }


}
