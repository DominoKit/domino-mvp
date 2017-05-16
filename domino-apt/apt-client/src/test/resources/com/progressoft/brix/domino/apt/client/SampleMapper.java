package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.history.RequestFromPath;
import com.progressoft.brix.domino.api.client.history.TokenizedPath;

public class SampleMapper implements RequestFromPath<AnnotatedClassWithPathAndCustomMapper> {

    @Override
    public AnnotatedClassWithPathAndCustomMapper buildRequest(TokenizedPath path) {
        return null;
    }
}