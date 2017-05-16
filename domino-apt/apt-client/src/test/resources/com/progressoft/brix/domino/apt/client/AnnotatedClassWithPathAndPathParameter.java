package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.PathParameter;
import com.progressoft.brix.domino.api.client.request.ClientRequest;

@Path(path = "somePath")
public class AnnotatedClassWithPathAndPathParameter extends ClientRequest<PresenterInterface> {

    private String value;

    public AnnotatedClassWithPathAndPathParameter(@PathParameter String value) {
        this.value = value;
    }

    @Override
    protected void process(PresenterInterface presenter) {

    }
}