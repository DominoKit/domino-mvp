package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.PathParameter;
import com.progressoft.brix.domino.api.client.request.ClientRequest;

@Path(path = "somePath")
public class AnnotatedClassWithPathAndParameterWithName extends ClientRequest<PresenterInterface> {

    private String value;

    public AnnotatedClassWithPathAndParameterWithName(@PathParameter(name = "someValue") String value) {
        this.value = value;
    }

    @Override
    protected void process(PresenterInterface presenter) {

    }
}