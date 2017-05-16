package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.request.ClientRequest;

@Path(path = "somePath", mapper = SampleMapper.class)
public class AnnotatedClassWithPathAndCustomMapper extends ClientRequest<PresenterInterface> {

    @Override
    protected void process(PresenterInterface presenter) {

    }
}