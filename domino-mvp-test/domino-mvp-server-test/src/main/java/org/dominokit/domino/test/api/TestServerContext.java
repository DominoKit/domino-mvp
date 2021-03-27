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
package org.dominokit.domino.test.api;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;

public interface TestServerContext extends TestRoutingContext {

  int getActualPort();

  String getCsrfToken();

  HttpRequest<Buffer> makeRequest(String path);

  HttpRequest<Buffer> makeRequest(String path, HttpMethod method);

  HttpRequest<Buffer> makeServiceRequest(String path);

  HttpRequest<Buffer> makeServiceRequest(String path, HttpMethod method);
}
