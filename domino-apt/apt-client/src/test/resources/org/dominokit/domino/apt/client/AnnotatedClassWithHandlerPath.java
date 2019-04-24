package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.shared.request.annotations.service.Path;

@Path(value="somePath")
public class AnnotatedClassWithHandlerPath extends ServerRequest<SomeRequest, SomeResponse> {
}