package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.request.ServerRequest;

import javax.ws.rs.HttpMethod;

@Path(value="somePath/{id}/{code}", method=HttpMethod.GET)
public class AnnotatedClassWithHandlerPathWithoutServiceRoot extends ServerRequest<SomeRequest, SomeResponse> {
}