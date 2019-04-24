package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.shared.request.annotations.service.Path;

import javax.ws.rs.HttpMethod;

@Path(value="somePath/{id}/{code}", method=HttpMethod.GET)
public class AnnotatedClassWithHandlerPathWithoutServiceRoot extends ServerRequest<SomeRequest, SomeResponse> {
}