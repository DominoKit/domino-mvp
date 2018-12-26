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
