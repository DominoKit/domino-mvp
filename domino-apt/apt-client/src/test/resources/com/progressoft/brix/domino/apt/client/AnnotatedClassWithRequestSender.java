package com.progressoft.brix.domino.apt.client;

import com.google.gwt.core.client.GWT;
import com.progressoft.brix.domino.api.client.request.RequestRestSender;
import com.progressoft.brix.domino.api.client.request.ServerRequestCallBack;
import com.progressoft.brix.domino.apt.client.AnnotatedClassWithHandlerPath;
import com.progressoft.brix.domino.apt.client.SomeRequest;
import com.progressoft.brix.domino.apt.client.SomeResponse;
import com.progressoft.brix.domino.api.client.annotations.RequestSender;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestSender(AnnotatedClassWithHandlerPath.class)
public class AnnotatedClassWithRequestSender implements RequestRestSender<SomeRequest> {

    public interface AnnotatedClassWithRequestSenderService extends RestService {
        @POST
        @Path("somePath")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        void send(SomeRequest request,
                  MethodCallback<SomeResponse> callback);
    }

    private AnnotatedClassWithRequestSenderService service = GWT.create(AnnotatedClassWithRequestSenderService.class);

    @Override
    public void send(SomeRequest request, ServerRequestCallBack callBack) {
        service.send(request, new MethodCallback<SomeResponse>() {
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