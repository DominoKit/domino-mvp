package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.request.ServerRequest;
import com.progressoft.brix.domino.api.client.annotations.Request;

import javax.ws.rs.HttpMethod;

@Request
@Path(value="somePath/{id}/{code}", serviceRoot ="someServiceRootPath", method=HttpMethod.GET)
public class AnnotatedClassWithHandlerPathWithServiceRoot extends ServerRequest<SomeRequest, SomeResponse> {
}