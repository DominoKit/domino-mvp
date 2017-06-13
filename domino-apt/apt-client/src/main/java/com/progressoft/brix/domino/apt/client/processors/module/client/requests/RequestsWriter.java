package com.progressoft.brix.domino.apt.client.processors.module.client.requests;

import com.progressoft.brix.domino.api.client.request.RequestRegistry;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.MethodBuilder;
import com.progressoft.brix.domino.apt.commons.ModifierBuilder;

import java.util.Set;

public class RequestsWriter {

    private final JavaSourceBuilder sourceBuilder;

    public RequestsWriter(JavaSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;
    }

    public void write(Set<String> requests){

        if(!requests.isEmpty()){
            sourceBuilder.imports(RequestRegistry.class.getCanonicalName());

            MethodBuilder methodBuilder = this.sourceBuilder.method("registerRequests");
            methodBuilder.annotate("@Override")
                    .withModifier(new ModifierBuilder().asPublic())
                    .returnsVoid()
                    .takes("RequestRegistry", "registry");
            requests.stream().map(this::parseEntry)
                    .forEach(e-> registerRequest(e, methodBuilder));
            methodBuilder.end();
        }
    }

    private void registerRequest(RequestEntry e, MethodBuilder methodBuilder) {
        importRequest(e);
        FullClassName request=new FullClassName(e.request);
        FullClassName presenter=new FullClassName(e.presenter);

        methodBuilder.line("registry.registerRequest("+request.asSimpleName()+".class.getCanonicalName(), "+presenter.asSimpleName()+".class.getCanonicalName());");

    }

    private void importRequest(RequestEntry e) {
        sourceBuilder.imports(e.request).imports(e.presenter);
    }

    private RequestEntry parseEntry(String request) {
        String[] requestPair=request.split(":");
        return new RequestEntry(requestPair[0], requestPair[1]);
    }

    private class RequestEntry {
        private final String request;
        private final String presenter;

        public RequestEntry(String request, String presenter) {
            this.request = request;
            this.presenter = presenter;
        }
    }
}
