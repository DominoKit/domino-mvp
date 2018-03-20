package org.dominokit.domino.apt.client;

import com.google.gwt.core.client.GWT;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@RequestSender(value = AnnotatedClassWithHandlerPath.class, customServiceRoot = true)
public class AnnotatedClassWithRequestSender implements RequestRestSender<SomeRequest> {

    public static final String SERVICE_ROOT_KEY="AnnotatedClassWithHandlerPath";
    public static final String SERVICE_ROOT="someServiceRootPath";

    private AnnotatedClassWithRequestSenderService service = GWT.create(AnnotatedClassWithRequestSenderService.class);

    public interface AnnotatedClassWithRequestSenderService extends RestService {
        @POST
        @Path("somePath")
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        void send(SomeRequest request,
                  MethodCallback<SomeResponse> callback);

    }

    @Override
    public void send(SomeRequest request, Map headers, ServerRequestCallBack callBack) {
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