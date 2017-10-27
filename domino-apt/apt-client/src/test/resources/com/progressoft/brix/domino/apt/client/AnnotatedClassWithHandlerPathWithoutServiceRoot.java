package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.request.ServerRequest;

import javax.ws.rs.HttpMethod;

@Path(value="somePath/{id}/{code}", method=HttpMethod.GET)
public class AnnotatedClassWithHandlerPathWithoutServiceRoot extends ServerRequest<SomeRequest, SomeResponse> {
}