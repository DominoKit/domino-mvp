package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.history.RequestFromPath;
import org.dominokit.domino.api.client.history.TokenizedPath;

public class SampleMapper implements RequestFromPath<AnnotatedClassWithPathAndCustomMapper> {

    @Override
    public AnnotatedClassWithPathAndCustomMapper buildRequest(TokenizedPath path) {
        return null;
    }
}