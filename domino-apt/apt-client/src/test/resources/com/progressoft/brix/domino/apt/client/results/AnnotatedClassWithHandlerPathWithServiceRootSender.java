package com.progressoft.brix.domino.apt.client;

import com.google.gwt.core.client.GWT;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithHandlerPathWithServiceRoot;
import com.progressoft.brix.domino.apt.client.SomeRequest;
import com.progressoft.brix.domino.apt.client.SomeResponse;
import com.progressoft.brix.domino.api.client.annotations.RequestSender;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import static java.util.Objects.*;
import org.fusesource.restygwt.client.*;
import com.progressoft.brix.domino.api.client.ServiceRootMatcher;

@RequestSender(value = AnnotatedClassWithHandlerPathWithServiceRoot.class, customServiceRoot=true)
public class AnnotatedClassWithHandlerPathWithServiceRootSender implements RequestRestSender<SomeRequest> {

    public static final String SERVICE_ROOT="someServiceRootPath";
    public static final String PATH="somePath/{id}/{code}";

    public interface AnnotatedClassWithHandlerPathWithServiceRootService extends RestService {
        @GET
        @Path(PATH)
        @Produces(MediaType.APPLICATION_JSON)
        void send(@PathParam("id") @Attribute("id") SomeRequest id, @PathParam("code") @Attribute("code") SomeRequest code,
                  MethodCallback<SomeResponse> callback);
    }

    private AnnotatedClassWithHandlerPathWithServiceRootService service = GWT.create(AnnotatedClassWithHandlerPathWithServiceRootService.class);

    @Override
    public void send(SomeRequest request, ServerRequestCallBack callBack) {
        ((RestServiceProxy)service).setResource(new Resource(SERVICE_ROOT));
        request.setRequestKey(request.getRequestKey()+"_xyz");
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
}