package com.progressoft.brix.domino.test.api;

import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.client.WebClient;

public class RequestTestClient<R extends ServerRequest, S extends ServerResponse> {

    private final TestContext context;
    private final int actualPort;
    private final WebClient webClient;
    private final Class<S> responseClass;
    private final String path;

    public RequestTestClient(Vertx vertx, TestContext context, int actualPort, String path, Class<S> responseClass) {
        this.context = context;
        this.actualPort = actualPort;
        this.path = path;
        this.responseClass = responseClass;
        webClient = WebClient.create(vertx);
    }

    public void executeRequest(R request, ResponseHandler<S> handler) {
        webClient.post(actualPort, "localhost", "/service/" + path).sendJson(request, context.asyncAssertSuccess(response -> {
            handler.onResponseReceived(Json.decodeValue(response.body(), responseClass));
        }));
    }


    public interface ResponseHandler<S> {
        void onResponseReceived(S response);
    }
}
