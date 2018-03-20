package org.dominokit.domino.apt.client;

import com.google.gwt.core.client.GWT;
import java.util.Map;
import javax.annotation.Generated;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.fusesource.restygwt.client.Attribute;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestService;
import org.fusesource.restygwt.client.RestServiceProxy;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.handlers.RequestPathProcessor")
@RequestSender(AnnotatedClassWithHandlerPathWithoutServiceRoot.class)
public class AnnotatedClassWithHandlerPathWithoutServiceRootSender implements RequestRestSender<SomeRequest> {

    public static final String PATH="somePath/{id}/{code}";

    private AnnotatedClassWithHandlerPathWithoutServiceRootService service = GWT.create(AnnotatedClassWithHandlerPathWithoutServiceRootService.class);

    @Override
    public void send(SomeRequest request, Map<String, String> headers, ServerRequestCallBack callBack) {
        ((RestServiceProxy)service).setResource(new Resource(ServiceRootMatcher.matchedServiceRoot(PATH), headers));
        service.send(request, request, new MethodCallback<SomeResponse>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                callBack.onFailure(throwable);
            }

            @Override
            public void onSuccess(Method method, SomeResponse response) {
                callBack.onSuccess(response);
            }
        });
    }

    public interface AnnotatedClassWithHandlerPathWithoutServiceRootService extends RestService {
        @GET
        @Path(PATH)
        @Produces(MediaType.APPLICATION_JSON)
        void send(@PathParam("id") @Attribute("id") SomeRequest id, @PathParam("code") @Attribute("code") SomeRequest code,
                  MethodCallback<SomeResponse> callback);
    }
}