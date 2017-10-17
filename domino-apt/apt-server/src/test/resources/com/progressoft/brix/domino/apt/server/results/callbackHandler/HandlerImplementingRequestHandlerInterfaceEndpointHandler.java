package com.progressoft.brix.domino.apt.server.callbackHandler;

import com.progressoft.brix.domino.api.server.ServerApp;
import com.progressoft.brix.domino.api.server.entrypoint.VertxEntryPointContext;
import com.progressoft.brix.domino.api.server.handler.CallbackRequestHandler;
import com.progressoft.brix.domino.api.shared.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.ServerResponse;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.server.EndpointsProcessor")
public class HandlerImplementingRequestHandlerInterfaceEndpointHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        try {
            ServerApp serverApp = ServerApp.make();
            ServerRequest requestBody;
            HttpMethod method=routingContext.request().method();
            if(HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method) || HttpMethod.PATCH.equals(method)){
                requestBody=Json.decodeValue(routingContext.getBodyAsString(), ServerRequest.class);
            }else {
                requestBody = new ServerRequest();
                requestBody.setRequestKey(ServerRequest.class.getCanonicalName());
            }
            serverApp.executeCallbackRequest(requestBody, new VertxEntryPointContext(routingContext, serverApp.serverContext().config(),
                    routingContext.vertx()), new CallbackRequestHandler.ResponseCallback() {
                @Override
                public void complete(Object response) {
                    routingContext.response()
                            .putHeader("content-type", "application/json")
                            .end(Json.encode((ServerResponse)response));
                }

                @Override
                public void redirect(String location) {
                    routingContext.response().setStatusCode(302).putHeader("Location",location).end();
                }
            });
        } catch (Exception e){
            routingContext.fail(e);
        }
    }
}