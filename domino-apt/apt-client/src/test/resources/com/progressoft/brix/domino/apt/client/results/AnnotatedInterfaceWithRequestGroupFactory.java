package com.progressoft.brix.domino.sample.items.client.requests;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.request.Response;
import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.VoidRequest;
import com.progressoft.brix.domino.apt.client.SomeRequest;
import com.progressoft.brix.domino.apt.client.SomeResponse;
import java.lang.Override;
import javax.annotation.Generated;

@Generated("com.progressoft.brix.domino.apt.client.processors.group.RequestGroupProcessor")
public class AnnotatedInterfaceWithRequestGroupFactory implements AnnotatedInterfaceWithRequestGroup {

    public static final AnnotatedInterfaceWithRequestGroupFactory INSTANCE = new AnnotatedInterfaceWithRequestGroupFactory();

    @Override
    public Response<SomeResponse> something(SomeRequest request) {
        return new AnnotatedInterfaceWithRequestGroup_something(request);
    }

    @Override
    public Response<SomeResponse> something2(SomeRequest request) {
        return new AnnotatedInterfaceWithRequestGroup_something2(request);
    }

    @Override
    public Response<SomeResponse> something3(SomeRequest request) {
        return new AnnotatedInterfaceWithRequestGroup_something3(request);
    }

    @Override
    public Response<SomeResponse> something4() {
        return new AnnotatedInterfaceWithRequestGroup_something4(RequestBean.VOID_REQUEST);
    }

    @Request
    @Path("testRoot/somePath")
    public class AnnotatedInterfaceWithRequestGroup_something extends ServerRequest<SomeRequest, SomeResponse> {
        AnnotatedInterfaceWithRequestGroup_something(SomeRequest request) {
            super(request);
        }
    }

    @Request(classifier = "somePath2")
    @Path("testRoot/somePath2")
    public class AnnotatedInterfaceWithRequestGroup_something2 extends ServerRequest<SomeRequest, SomeResponse> {
        AnnotatedInterfaceWithRequestGroup_something2(SomeRequest request) {
            super(request);
        }
    }

    @Request
    @Path(method = "GET", value = "testRoot/somePath3")
    public class AnnotatedInterfaceWithRequestGroup_something3 extends ServerRequest<SomeRequest, SomeResponse> {
        AnnotatedInterfaceWithRequestGroup_something3(SomeRequest request) {
            super(request);
        }
    }

    @Request
    @Path("testRoot/somePath4")
    public class AnnotatedInterfaceWithRequestGroup_something4 extends ServerRequest<VoidRequest, SomeResponse> {
        AnnotatedInterfaceWithRequestGroup_something4(VoidRequest request) {
            super(request);
        }
    }
}
