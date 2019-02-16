package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.service.Path;
import org.dominokit.domino.api.client.request.ServerRequest;
import org.dominokit.domino.api.client.annotations.service.Request;

import javax.ws.rs.HttpMethod;

@Request
@Path(value="somePath/{id}/{code}", serviceRoot ="someServiceRootPath", method=HttpMethod.GET)
public class AnnotatedClassWithHandlerPathWithServiceRoot extends ServerRequest<SomeRequest, SomeResponse> {
}