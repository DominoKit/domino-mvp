package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;

@Path(value="somePath/{id}/{code}", serviceRoot ="someServiceRootPath", method=HttpMethod.GET)
public class AnnotatedClassWithHandlerPathWithServiceRoot extends ClientServerRequest<PresenterInterface, SomeRequest, SomeResponse> {

    @Override
    protected void process(PresenterInterface presenter, SomeRequest serverArgs, SomeResponse response) {

    }

    @Override
    public SomeRequest buildArguments() {
        return null;
    }
}