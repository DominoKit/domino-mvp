package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.request.ClientRequest;

@Request
public class AnnotatedClassWithRequest extends ClientRequest<PresenterInterface> {

    @Override
    protected void process(PresenterInterface presenter) {

    }
}