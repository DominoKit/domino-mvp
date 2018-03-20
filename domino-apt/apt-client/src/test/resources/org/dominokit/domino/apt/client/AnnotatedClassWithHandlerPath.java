package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.Path;
import org.dominokit.domino.api.client.request.ServerRequest;

@Path(value="somePath")
public class AnnotatedClassWithHandlerPath extends ServerRequest<SomeRequest, SomeResponse> {
}