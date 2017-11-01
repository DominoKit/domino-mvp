package com.progressoft.brix.domino.sample.items.client.requests;

import com.progressoft.brix.domino.api.client.annotations.Classifier;
import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.RequestFactory;
import com.progressoft.brix.domino.api.client.request.Response;
import com.progressoft.brix.domino.apt.client.SomeRequest;
import com.progressoft.brix.domino.apt.client.SomeResponse;

import javax.ws.rs.HttpMethod;

@RequestFactory("testRoot/")
public interface AnnotatedInterfaceWithRequestGroup {

    @Path("somePath")
    Response<SomeResponse> something(SomeRequest request);

    @Path("somePath2")
    @Classifier("somePath2")
    Response<SomeResponse> something2(SomeRequest request);

    @Path(value = "somePath3", method = HttpMethod.GET)
    Response<SomeResponse> something3(SomeRequest request);

    @Path(value = "somePath4")
    Response<SomeResponse> something4();
}
