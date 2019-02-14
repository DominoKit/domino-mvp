package org.dominokit.domino.apt.client;

import java.util.Arrays;
import java.util.Map;
import javax.annotation.Generated;
import org.dominokit.domino.api.client.ServiceRootMatcher;
import org.dominokit.domino.api.client.annotations.RequestSender;
import org.dominokit.domino.api.client.request.RequestRestSender;
import org.dominokit.domino.api.client.request.ServerRequestCallBack;
import org.dominokit.domino.api.shared.request.ArrayResponse;
import org.dominokit.rest.client.FailedResponse;
import org.dominokit.rest.shared.RestfulRequest;
import org.test.sample.xyz.shared.request.XyzRequest;
import org.test.sample.xyz.shared.request.XyzRequest_MapperImpl;
import org.test.sample.xyz.shared.response.XyzResponse;
import org.test.sample.xyz.shared.response.XyzResponse_MapperImpl;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.resources.RequestPathProcessor")
@RequestSender(value = AnnotatedClassWithHandlerPath.class, customServiceRoot = true)
public class AnnotatedClassWithRequestSender implements RequestRestSender<SomeRequest> {

    public static final String SERVICE_ROOT_KEY="AnnotatedClassWithHandlerPath";
    public static final String SERVICE_ROOT="someServiceRootPath";

    public static final String PATH = "XyzRequest";

    private static final Integer[] SUCCESS_CODES = new Integer[]{200,201,202,203,204};

    @Override
    public void send(SomeRequest request, Map<String, String> headers,
                     Map parameters, ServerRequestCallBack callBack) {
        RestfulRequest.post(ServiceRootMatcher.matchedServiceRoot(PATH) + PATH)
                .putHeaders(headers)
                .onSuccess(response -> {
                            if(Arrays.stream(SUCCESS_CODES).anyMatch(code -> code.equals(response.getStatusCode()))) {
                                SomeResponse[] items = SomeResponse.INSTANCE.readArray(response.getBodyAsString(), length -> new SomeResponse[length]);
                                callBack.onSuccess(new ArrayResponse<>(items));
                            }
                            else {
                                callBack.onFailure(new FailedResponse(response.getStatusCode(), response.getBodyAsString()));
                            }
                        }
                ).onError(callBack::onFailure)
                .sendJson(SomeRequest_MapperImpl.INSTANCE.write(request));
    }
}
