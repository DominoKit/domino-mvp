package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;

@Path(value="somePath")
public class AnnotatedClassWithHandlerPath extends ClientServerRequest<SomeRequest, SomeResponse> {
}