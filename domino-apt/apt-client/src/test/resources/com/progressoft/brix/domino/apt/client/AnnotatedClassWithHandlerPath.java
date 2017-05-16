package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.HandlerPath;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.apt.client.SomeRequest;
import com.progressoft.brix.domino.apt.client.SomeResponse;
import com.progressoft.brix.domino.apt.client.PresenterInterface;

@HandlerPath("somePath")
public class AnnotatedClassWithHandlerPath extends ClientServerRequest<PresenterInterface, SomeRequest, SomeResponse> {

    @Override
    protected void process(PresenterInterface presenter, SomeRequest serverArgs, SomeResponse response) {

    }

    @Override
    public SomeRequest buildArguments() {
        return null;
    }
}