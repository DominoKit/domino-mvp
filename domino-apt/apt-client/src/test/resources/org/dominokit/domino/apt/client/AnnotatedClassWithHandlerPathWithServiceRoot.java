package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.shared.request.annotations.service.Path;
import org.dominokit.domino.api.shared.request.service.annotations.Request;

import javax.ws.rs.HttpMethod;

@Request
@Path(value="somePath/{id}/{code}", serviceRoot ="someServiceRootPath", method=HttpMethod.GET)
public class AnnotatedClassWithHandlerPathWithServiceRoot extends ServerRequest<SomeRequest, SomeResponse> {
}