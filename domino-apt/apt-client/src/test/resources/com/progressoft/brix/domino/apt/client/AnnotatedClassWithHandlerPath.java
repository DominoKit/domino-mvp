package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.request.ServerRequest;

@Path(value="somePath")
public class AnnotatedClassWithHandlerPath extends ServerRequest<SomeRequest, SomeResponse> {
}