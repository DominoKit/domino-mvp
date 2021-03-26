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
package org.dominokit.domino.service.discovery.type;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.servicediscovery.Record;
import org.dominokit.domino.service.discovery.DataSourceServiceDiscoveryTest;
import org.dominokit.domino.service.discovery.configuration.BaseServiceConfiguration;
import org.dominokit.domino.service.discovery.configuration.DataSourceServiceConfiguration;
import org.junit.Test;

public class JDBCServiceDiscoveryTest extends DataSourceServiceDiscoveryTest {

  private void publishJDBCDataSourceService(DataSourceServiceConfiguration configuration) {
    publishJDBCDataSourceService(configuration, ar -> {});
  }

  private void publishJDBCDataSourceService(
      DataSourceServiceConfiguration configuration, Handler<AsyncResult<Record>> handler) {
    vertxServiceDiscovery.jdbc().publish(configuration, handler);
  }

  @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
  public void
      givenServiceDiscovery_whenPublishingJDBCDataSourceServiceWithNullName_thenShouldThrowException()
          throws Exception {
    publishJDBCDataSourceService(new DataSourceServiceConfiguration(null, dataSourceLocation));
  }

  @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
  public void
      givenServiceDiscovery_whenPublishingJDBCDataSourceServiceWithEmptyName_thenShouldThrowException()
          throws Exception {
    publishJDBCDataSourceService(
        new DataSourceServiceConfiguration(EMPTY_STRING, dataSourceLocation));
  }

  @Test(expected = BaseServiceConfiguration.InvalidServiceNameException.class)
  public void
      givenServiceDiscovery_whenPublishingJDBCDataSourceServiceWithOnlySpacesName_thenShouldThrowException()
          throws Exception {
    publishJDBCDataSourceService(
        new DataSourceServiceConfiguration(ONLY_SPACES, dataSourceLocation));
  }

  @Test(expected = DataSourceServiceConfiguration.NullServiceLocationException.class)
  public void
      givenServiceDiscovery_whenPublishingJDBCDataSourceServiceWithNullUrl_thenShouldThrowException()
          throws Exception {
    publishJDBCDataSourceService(new DataSourceServiceConfiguration(SERVICE_NAME, null));
  }

  @Test
  public void
      givenServiceDiscovery_whenPublishingJDBCDataSourceService_thenTheServiceShouldBePublished(
          TestContext context) throws Exception {
    publishJDBCDataSourceService(dataSourceConfiguration, context.asyncAssertSuccess());
  }

  @Test
  public void
      givenServiceDiscovery_whenPublishingJDBCDataSourceServiceWithMetadata_thenTheServiceShouldBePublishedWithThatMetadata(
          TestContext context) throws Exception {
    publishJDBCDataSourceService(
        dataSourceConfiguration.metadata(metadata),
        context.asyncAssertSuccess(
            record -> {
              context.assertEquals(VALUE, record.getMetadata().getString(KEY));
            }));
  }

  @Test
  public void
      givenServiceDiscovery_whenPublishingMessageSourceAndAskingForItsClient_thenShouldGetTheClient(
          TestContext context) throws Exception {
    publishJDBCDataSourceService(dataSourceConfiguration);
    vertxServiceDiscovery.jdbc().getClient(filter(), context.asyncAssertSuccess());
  }

  @Test
  public void
      givenServiceDiscovery_whenPublishingMessageSourceAndAskingForItsClientWithConfiguration_thenShouldGetTheClient(
          TestContext context) throws Exception {
    publishJDBCDataSourceService(dataSourceConfiguration.metadata(metadata));
    vertxServiceDiscovery.jdbc().getClient(filter(), metadata, context.asyncAssertSuccess());
  }

  @Test
  public void
      givenServiceDiscovery_whenPublishingMessageSourceAndAskingForItsClientByJson_thenShouldGetTheClient(
          TestContext context) throws Exception {
    publishJDBCDataSourceService(dataSourceConfiguration);
    vertxServiceDiscovery.jdbc().getClient(jsonFilter, context.asyncAssertSuccess());
  }

  @Test
  public void
      givenServiceDiscovery_whenPublishingMessageSourceAndAskingForItsClientByJsonWithConfiguration_thenShouldGetTheClient(
          TestContext context) throws Exception {
    publishJDBCDataSourceService(dataSourceConfiguration.metadata(metadata));
    vertxServiceDiscovery.jdbc().getClient(jsonFilter, metadata, context.asyncAssertSuccess());
  }
}
