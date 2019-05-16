package org.dominokit.domino.api.shared.request;

public class StringWriter implements RequestWriter<String> {
    @Override
    public String write(String request) {
        return request;
    }
}
