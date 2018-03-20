package org.dominokit.domino.apt.client;

import javax.annotation.Generated;
import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.annotations.Request;
import org.dominokit.domino.api.client.request.Response;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;
import org.dominokit.domino.api.shared.request.VoidRequest;

/**
 * This is generated class, please don't modify
 */
@Generated("org.dominokit.domino.apt.client.processors.group.RequestFactoryProcessor")
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
    public Response<ResponseBean> something3(RequestBean request) {
        return new AnnotatedInterfaceWithRequestGroup_something3(request);
    }

    @Override
    public Response<SomeResponse> something4() {
        return new AnnotatedInterfaceWithRequestGroup_something4(RequestBean.VOID_REQUEST);
    }

    @Override
    public Response<SomeResponse> something5() {
        return new AnnotatedInterfaceWithRequestGroup_something5(RequestBean.VOID_REQUEST);
    }

    @Request
    @Path("testRoot/somePath")
    public class AnnotatedInterfaceWithRequestGroup_something extends ServerRequest<SomeRequest, SomeResponse> {
        AnnotatedInterfaceWithRequestGroup_something(SomeRequest request) {
            super(request);
        }
    }

    @Request
    @Path("testRoot/somePath2")
    public class AnnotatedInterfaceWithRequestGroup_something2 extends ServerRequest<SomeRequest, SomeResponse> {
        AnnotatedInterfaceWithRequestGroup_something2(SomeRequest request) {
            super(request);
        }
    }

    @Request
    @Path(method = "GET", value = "testRoot/somePath3")
    public class AnnotatedInterfaceWithRequestGroup_something3 extends ServerRequest<RequestBean, ResponseBean> {
        AnnotatedInterfaceWithRequestGroup_something3(RequestBean request) {
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

    @Request
    @Path("testRoot/somePath5")
    public class AnnotatedInterfaceWithRequestGroup_something5 extends ServerRequest<VoidRequest, SomeResponse> {
        AnnotatedInterfaceWithRequestGroup_something5(VoidRequest request) {
            super(request);
        }
    }
}
