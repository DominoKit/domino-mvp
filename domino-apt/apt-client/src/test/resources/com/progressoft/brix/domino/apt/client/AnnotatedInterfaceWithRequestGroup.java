package com.progressoft.brix.domino.sample.items.client.requests;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.RequestFactory;
import com.progressoft.brix.domino.api.client.request.Response;
import com.progressoft.brix.domino.api.shared.request.RequestBean;
import com.progressoft.brix.domino.api.shared.request.ResponseBean;
import com.progressoft.brix.domino.apt.client.SomeRequest;
import com.progressoft.brix.domino.apt.client.SomeResponse;

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
