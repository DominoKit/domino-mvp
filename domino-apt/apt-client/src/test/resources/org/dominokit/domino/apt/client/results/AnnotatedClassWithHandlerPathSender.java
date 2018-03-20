package org.dominokit.domino.apt.client;

import com.google.gwt.core.client.GWT;
import org.akab.engine.core.api.client.request.RequestRestSender;
import org.akab.engine.core.api.client.request.ServerRequestCallBack;
import org.akab.engine.core.annotation.processor.client.AnnotatedClassWithHandlerPath;
import org.akab.engine.core.annotation.processor.client.SomeRequest;
import org.akab.engine.core.annotation.processor.client.SomeResponse;
import org.akab.engine.core.api.client.annotations.RequestSender;
import org.dominokit.domino.apt.client.AnnotatedClassWithHandlerPath;
import org.dominokit.domino.apt.client.SomeRequest;
import org.dominokit.domino.apt.client.SomeResponse;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestSender(AnnotatedClassWithHandlerPath.class)
public class AnnotatedClassWithHandlerPathSender implements RequestRestSender<SomeRequest> {

    private AnnotatedClassWithHandlerPathSenderService service = GWT.create(AnnotatedClassWithHandlerPathSenderService.class);

    public interface AnnotatedClassWithHandlerPathSenderService extends RestService {
        @POST
        @Path("somePath")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        void send(SomeRequest request,
                  MethodCallback<SomeResponse> callback);

    }

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