package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.shared.request.annotations.service.Path;
import org.dominokit.domino.api.shared.request.service.annotations.RequestFactory;
import org.dominokit.domino.api.shared.request.RequestBean;
import org.dominokit.domino.api.shared.request.ResponseBean;

import javax.ws.rs.HttpMethod;

@RequestFactory("testRoot/")
public interface AnnotatedInterfaceWithRequestGroup {

    @Path("somePath")
    Response<SomeResponse> something(SomeRequest request);

    @Path("somePath2")
    Response<SomeResponse> something2(SomeRequest request);

    @Path(value = "somePath3", method = HttpMethod.GET)
    Response<ResponseBean> something3(RequestBean request);

    @Path(value = "somePath4")
    Response<SomeResponse> something4();

    @Path(value = "somePath5")
    Response<SomeResponse> something5();
}
